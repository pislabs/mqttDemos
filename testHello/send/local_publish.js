const mqtt = require("mqtt");

const serverHost = "mqtt://127.0.0.1:1883";

console.log(`正在连接...${serverHost}`);

const client = mqtt.connect(serverHost, {
  clientId: "mqtt_identical_1",
  clean: false,
});

client.on("connect", async (connack) => {
  console.log(
    `return code: ${connack.returnCode}, sessionPresent: ${connack.sessionPresent}`
  );

  if (connack.returnCode !== 0) {
    console.log(`Connection failed: ${connack.returnCode}`);
    return;
  }

  console.log("client.publish ------>");

  client.publish(
    "home/2ndfloor/201/temperature",
    JSON.stringify({ current: 25 }),
    { qos: 1 },
    (err) => {
      if (err) {
        console.log("Publish failed ---->");
        return;
      }

      console.log("Publish finished");
      client.end();
    }
  );

  // await client
  //   .publishAsync(
  //     "home/2ndfloor/201/temperature",
  //     JSON.stringify({ current: 25 }),
  //     { qos: 1 }
  //   )
  //   .then(() => {
  //     console.log("Publish finished");
  //     client.end();
  //   })
  //   .catch((err) => {
  //     console.log("Publish failed ---->", err);
  //   });
});

client.on("offline", function () {
  console.log(`client went offline`);
});
