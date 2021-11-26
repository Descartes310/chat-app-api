# Getting Started with Chat Demo API


## Deployment instructions

1. `git clone git@github.com:Descartes310/chat-app-api.git`
2. `cd chat-app-api`
3. `mvn clean`
4. `mvn spring-boot:run` api will start on port `8001`

## Kafka Server

1. Download kafka 2.12-3.0.0 on `https://www.apache.org/dyn/closer.cgi?path=/kafka/3.0.0/kafka_2.12-3.0.0.tgz`
2. Extract kafka_2.12-3.0.0.tgz
3. `cd kafka_2.12-3.0.0/bin`
4. In one terminal, run `./zookeeper-server-start.sh ../config/zookeeper.properties`
4. In one terminal, run `./kafka-server-start.sh ../config/server.properties`

## Next steps

1. Add Group management
2. Add unread messages management
