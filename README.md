# Build docker image

Follow the below steps to package application into docker image and to run it.

```
./mvnw clean package
```

```
docker build -f src/main/docker/Dockerfile.jvm -t getting-started-sftp:latest .
```

```
docker run -i --rm -p 8080:8080 quarkus/getting-started-jvm
```
