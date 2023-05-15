FROM openjdk:8-jdk-alpine

RUN apk update && apk add bash && apk add maven && apk add python3

COPY spark-3.2.1-bin-hadoop3.2 /spark

RUN cd /spark \
    && mvn clean compile assembly:single

RUN cp /spark/target/spark-structured-streaming-filter-1.0-SNAPSHOT-jar-with-dependencies.jar /spark/structured-streaming.jar


FROM openjdk:8-jdk-alpine

