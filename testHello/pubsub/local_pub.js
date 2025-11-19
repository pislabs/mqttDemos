const yargs = require("yargs");
const { hideBin } = require("yargs/helpers");

const mqtt = require("mqtt");

const args = yargs(hideBin(process.argv)).parse();

const serverHost = "mqtt://127.0.0.1:1883";
const topic = "home/2ndfloor/201/temperature";
const payload = { current: 10 };

const qos = args?.qos === 0 ? 0 : args?.qos || 1;

console.log(`正在连接...${serverHost} ${typeof args?.qos}`);

const client = mqtt.connect(serverHost, {
  clientId: "mqtt_sample_publisher_1",
  clean: false,
});

client.on("connect", (connack) => {
  if (connack.returnCode) {
    console.log(`Connection failed: ${connack.returnCode}`);
    return;
  }

  console.log(`publishing qos:${qos}`);

  client.publish(topic, JSON.stringify(payload), { qos }, (err) => {
    if (err) {
      console.log("Publish failed", err);
      return;
    }

    console.log("Publish finished");
    client.end();
  });
});
