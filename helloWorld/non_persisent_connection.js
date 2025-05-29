const mqtt = require("mqtt");

const serverHost = "mqtt://mqtt.eclipseprojects.io";

console.log(`正在连接...${serverHost}`);

const client = mqtt.connect(serverHost, {
  clientId: "mqtt_sample_id_1",
  clean: false,
});

client.on("connect", function (connack) {
  console.log(
    `return code: ${connack.returnCode}, sessionPresent: ${connack.sessionPresent}`
  );

  client.end();
});
