const mqtt = require("mqtt");

const serverHost = "mqtt://127.0.0.1:1883";
console.log(`正在连接...${serverHost}`);

const client = mqtt.connect(serverHost, {
  clientId: "mqtt_sample_publisher_chapter_8",
  clean: false,
  will: {
    topic: "client/status",
    qos: 1,
    retain: true,
    payload: JSON.stringify({ status: "offline" }),
  },
});

client.on("connect", function (connack) {
  console.log(`已连接...${connack.returnCode}`);

  if (connack.returnCode == 0) {
    client.publish("client/status", JSON.stringify({ status: "online" }), {
      qos: 1,
      retain: 1,
    });
  } else {
    console.log(`Connection failed: ${connack.returnCode}`);
  }
});
