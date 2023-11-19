FROM gradle:jdk17-alpine AS build

WORKDIR /app

COPY . .

RUN ls docker-compose.yaml
RUN ./gradlew clean
RUN ./gradlew build -Dquarkus.package.type=native

FROM registry.access.redhat.com/ubi8/ubi-minimal:8.8 AS base

WORKDIR /work/

COPY --from=build build/*-runner /work/application

RUN chown 1001 /work \
    && chmod "g+rwX" /work \
    && chown 1001:root /work

EXPOSE 8080
USER 1001

ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0"]