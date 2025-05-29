const mqtt = require("mqtt");

const serverHost = "mqtt://mqtt.eclipseprojects.io";

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
