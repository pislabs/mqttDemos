const mqtt = require("mqtt");

const serverHost = "mqtts://be1f1690.ala.cn-hangzhou.emqxsl.cn:8883";

console.log(`正在连接...${serverHost}`);

const client = mqtt.connect(serverHost, {
  clientId: "mqtt_sample_id_1",
  clean: false, // 重新连接复用之前的session
  username: "rayl",
  password: "123456",
});

client.on("connect", function (connack) {
  console.log(
    `return code: ${connack.returnCode}, sessionPresent: ${connack.sessionPresent}`
  );

  client.end();
});
