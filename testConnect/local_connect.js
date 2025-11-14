const mqtt = require("mqtt");

const serverHost = "mqtt://127.0.0.1:1883";

console.log(`正在连接...${serverHost}`);

const client = mqtt.connect(serverHost, {
  clientId: "mqtt_sample_id_1",
  clean: false, // 重新连接复用之前的session
});

client.on("connect", function (connack) {
  console.log(
    `return code: ${connack.returnCode}, sessionPresent: ${connack.sessionPresent}`
  );

  client.end();
});
