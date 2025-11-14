# 启动

## docker 安装启动

```bash

cd /Users/rayl/AppData/eqmx

docker run -d --name emqx-enterprise \
  --hostname node1.emqx.com \
  -e "EMQX_NODE_NAME=emqx@node1.emqx.com" \
  -p 1883:1883 -p 8083:8083 \
  -p 8084:8084 -p 8883:8883 \
  -p 18083:18083 \
  -v $PWD/data:/opt/emqx/data \
  -v $PWD/log:/opt/emqx/log \
  emqx/emqx-enterprise:6.0.1

# 访问：http://localhost:18083/
# 默认密码：admin/public -> admin/1234Qwer

```

参考：

- https://docs.emqx.com/en/emqx/latest/deploy/install-docker.html
- https://docs.emqx.com/zh/emqx/v6.0/getting-started/getting-started.html
