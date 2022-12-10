FROM clojure:openjdk-11-lein-slim-buster as build
COPY . /src
WORKDIR /src
RUN lein uberjar

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /src/target/*-standalone.jar ./clojure-crud.jar
CMD ["java", "-jar", "/app/clojure-crud.jar"]
