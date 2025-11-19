const yargs = require("yargs");
const { hideBin } = require("yargs/helpers");

const mqtt = require("mqtt");

const args = yargs(hideBin(process.argv)).parse();

const serverHost = "mqtt://127.0.0.1:1883";
const topic = "home/2ndfloor/201/temperature";

const qos = args?.qos === 0 ? 0 : args?.qos || 1;

console.log(`正在连接...${serverHost}`);

const client = mqtt.connect(serverHost, {
  clientId: "mqtt_sample_subscriber_id_1",
  clean: true,
});

client.on("connect", function (connack) {
  if (connack.returnCode) {
    console.log(`Connection failed: ${connack.returnCode}`);
    return;
  }

  if (connack.sessionPresent) {
    console.log(`connack.sessionPresent: ${connack.sessionPresent}`);
    return;
  }

  console.log(`subscribing qos:${qos}`);

  client.subscribe(topic, { qos }, (err, granted) => {
    if (err) {
      console.log("subscribe failed", err);
      return;
    }

    console.log(
      `subscribe succeeded with ${granted[0]?.topic}, qos: ${granted[0]?.qos}`
    );

    client.on("packetsend", (packet) => {
      console.log(`send: ${packet.cmd}`);
    });

    client.on("packetreceive", (packet) => {
      console.log(`receive: ${packet.cmd}`);
    });
  });
});

client.on("message", function (_, message, _) {
  const jsonPayload = JSON.parse(message.toString());
  console.log(`current temperature is ${jsonPayload.current}`);

  if (jsonPayload.current === 0) {
    client.unsubscribe(topic, (err) => {
      if (err) {
        console.log("unsubscribe failed", err);
        return;
      }

      console.log(`unsubscribe succeeded`);
    });

    return;
  }

  if (jsonPayload.current === 1) {
    client.end();
    console.log(`client end`);
  }
});
