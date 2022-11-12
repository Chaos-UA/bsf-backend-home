### Getting Started

* You can find task description in `./task-description.md`
* Documentation of API is done by using swagger open API. Please check: `./account-service-open-api.yaml`. 
You can copy content to https://editor.swagger.io to see documentation
* Application created by using `jdk-17` (tested with `jdk-11`)
* Application database `PostgreSQL 14`
* App uses JOOQ ORM framework to generate DB models. 
You may need to reimport maven project once in your IDE after build to resolve them.

### Application default properties. Please use env variables to change app property
* SPRING_DATASOURCE_URL=`jdbc:postgresql://localhost:5432/account-service`
* SPRING_DATASOURCE_USERNAME=`username`
* SPRING_DATASOURCE_PASSWORD=`password`

### Build & start java application
1. Build app: `./mvnw clean package`
2. Command to start app: 
```
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/account-service
export SPRING_DATASOURCE_USERNAME=username
export SPRING_DATASOURCE_PASSWORD=password
java -jar ./target/account-service-1.0-SNAPSHOT.jar`
```

### Build docker image and start as docker app
1. To build app: `./mvnw clean package`
2. To build docker image: `docker build -t account-service:1.0 .`
3. Start app in docker by providing correct properties:
```
docker run -p 8080:8080 \
    --rm \
    -e SPRING_DATASOURCE_URL=jdbc:postgresql://192.168.220.130:5432/account-service \
    -e SPRING_DATASOURCE_USERNAME=username \
    -e SPRING_DATASOURCE_PASSWORD=password \
    -t account-service:1.0
```

### Build docker image and start app and db in docker compose
1. To build app: `./mvnw clean package`
2. To build docker image and start app: `docker compose up`

### Pre-created accounts IDs in DB
```
50c19f57-6a65-4e85-ac40-2ebccf295b80
50c19f57-6a65-4e85-ac40-2ebccf295b81
50c19f57-6a65-4e85-ac40-2ebccf295b82
50c19f57-6a65-4e85-ac40-2ebccf295b83
```
