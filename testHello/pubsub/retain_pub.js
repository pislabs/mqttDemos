const mqtt = require("mqtt");

const serverHost = "mqtt://127.0.0.1:1883";
console.log(`正在连接...${serverHost}`);

const client = mqtt.connect(serverHost, {
  clientId: "mqtt_sample_publisher_1",
  clean: false,
});

client.on("connect", (connack) => {
  if (connack.returnCode == 0) {
    client.publish(
      "home/2ndfloor/201/temperature",
      JSON.stringify({ current: 25 }),
      { qos: 0, retain: 1 },
      (err) => {
        if (err == undefined) {
          console.log("Publish finished");
          client.end();
        } else {
          console.log("Publish failed");
        }
      }
    );
  } else {
    console.log("Connection failed: ${connack.returnCode}");
  }
});
