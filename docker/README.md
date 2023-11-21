### Prerequisite
- [docker desktop install](https://docs.docker.com/desktop/install/mac-install/)

### Before start & start
```shell
# 현재 떠있는 docker container 확인 가능
$ docker ps
```

```shell
$ docker volume create postgre-pg-vol
```

```shell
$ docker network create postgre-pg-network
```

```shell
# docker-compose.yml 파일이 있는 경로에서 왜안대!
$ docker compose up -d
```