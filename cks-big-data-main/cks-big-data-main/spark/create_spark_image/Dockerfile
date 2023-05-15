FROM openjdk:8-alpine
RUN apk --update add wget tar bash nano maven
RUN wget https://downloads.apache.org/spark/spark-3.3.2/spark-3.3.2-bin-hadoop2.tgz
RUN tar -xzf spark-3.3.2-bin-hadoop2.tgz && \
    mv  spark-3.3.2-bin-hadoop2 /spark && \
    rm  spark-3.3.2-bin-hadoop2.tgz


ENV SCALA_VERSION=2.12.0 \
    SCALA_HOME=/usr/share/scala

# NOTE: bash is used by scala/scalac scripts, and it cannot be easily replaced with ash.
RUN apk add --no-cache --virtual=.build-dependencies wget ca-certificates && \
    apk add --no-cache bash curl jq && \
    cd "/tmp" && \
    wget --no-verbose "https://downloads.typesafe.com/scala/${SCALA_VERSION}/scala-${SCALA_VERSION}.tgz" && \
    tar xzf "scala-${SCALA_VERSION}.tgz" && \
    mkdir "${SCALA_HOME}" && \
    rm "/tmp/scala-${SCALA_VERSION}/bin/"*.bat && \
    mv "/tmp/scala-${SCALA_VERSION}/bin" "/tmp/scala-${SCALA_VERSION}/lib" "${SCALA_HOME}" && \
    ln -s "${SCALA_HOME}/bin/"* "/usr/bin/" && \
    apk del .build-dependencies && \
    rm -rf "/tmp/"*

COPY basicAuthenticationFilter.jar /spark/jars/basicAuthenticationFilter.jar
COPY start-master.sh /start-master.sh
COPY start-worker.sh /start-worker.sh  
COPY start-sub.sh /start-sub.sh    
COPY start-sub-copy.sh /start-sub-copy.sh 
RUN chmod +x start-master.sh
RUN chmod +x start-worker.sh
RUN chmod +x start-sub.sh
RUN chmod +x start-sub-copy.sh
