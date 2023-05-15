FROM jp_spark:2.4.7

ENV SCALA_VERSION=2.11.0 \
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
 
 RUN apk add --no-cache --virtual=build-dependencies curl tar && \
    curl -sL "http://dl.bintray.com/sbt/native-packages/sbt/0.13.9/sbt-0.13.9.tgz" | gunzip | tar -x -C /usr/local && \
    ln -s /usr/local/sbt/bin/sbt /usr/local/bin/sbt && \
    chmod 0755 /usr/local/bin/sbt


COPY ./filter2 /filter2
RUN cd /filter2 \
    && sbt clean && sbt assembly 

RUN mv /filter2/target/scala-2.11/Filter2.jar  /Filter2.jar
RUN chmod 777 /Filter2.jar