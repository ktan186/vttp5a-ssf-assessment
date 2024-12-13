FROM eclipse-temurin:23-noble AS builder

WORKDIR /src

# copy files
COPY mvnw .
COPY pom.xml .

COPY .mvn .mvn
COPY src src

# make mvnw executable
RUN chmod a+x mvnw && /src/mvnw package -Dmaven.test.skip=true

# using jre as it does not have runtime, better security. no need for runtime as image already built above
FROM eclipse-temurin:23-jre-noble

WORKDIR /app

COPY --from=builder /src/target/noticeboard-0.0.1-SNAPSHOT.jar app.jar

# check if curl command is available
RUN apt update && apt install -y curl

ENV PORT=3000
# placeholder for api, will override later by env variable in railway
ENV api.endpoint=https://publishing-production-d35a.up.railway.app/notice

EXPOSE ${PORT}

HEALTHCHECK --interval=60s --timeout=5s --start-period=120s --retries=3 \
   CMD curl -s -f http://localhost:${PORT}/status || exit 1

ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar