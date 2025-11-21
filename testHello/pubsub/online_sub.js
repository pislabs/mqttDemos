const mqtt = require("mqtt");

const serverHost = "mqtt://127.0.0.1:1883";
console.log(`正在连接...${serverHost}`);

const client = mqtt.connect(serverHost, {
  clientId: "mqtt_sample_subscriber_id_chapter_8_2",
  clean: false,
});

client.on("connect", function (connack) {
  console.log(`已连接...${connack.returnCode}`);

  client.subscribe("client/status", { qos: 1 });
});

client.on("message", function (_, message) {
  const jsonPayload = JSON.parse(message.toString());
  console.log(`client is ${jsonPayload.status}`);
});
