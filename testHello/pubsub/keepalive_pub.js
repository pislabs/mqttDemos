const mqtt = require("mqtt");
const dateTime = require("node-datetime");

const serverHost = "mqtt://127.0.0.1:1883";
console.log(`正在连接...${serverHost}`);

const client = mqtt.connect(serverHost, {
  clientId: "mqtt_sample_id_chapter_9",
  clean: false,
  keepalive: 5,
});

client.on("connect", function () {
  client.on("packetsend", function (packet) {
    console.log(`${dateTime.create().format("H:M:S")}: send ${packet.cmd}`);
  });

  client.on("packetreceive", function (packet) {
    console.log(`${dateTime.create().format("H:M:S")}: receive ${packet.cmd}`);
  });

  setInterval(function () {
    client.publish("foo/bar", "test");
  }, 4 * 1000);
});
