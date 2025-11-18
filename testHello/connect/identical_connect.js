const mqtt = require("mqtt");

const serverHost = "mqtt://127.0.0.1:1883";

console.log(`正在连接...${serverHost}`);

const client = mqtt.connect(serverHost, {
  clientId: "mqtt_identical_1",
});

client.on("connect", function (connack) {
  console.log(
    `return code: ${connack.returnCode}, sessionPresent: ${connack.sessionPresent}`
  );
});

client.on("offline", function () {
  console.log(`client went offline`);
});
