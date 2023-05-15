#!/bin/bash
mkdir -p $SPARK_MASTER_LOG
ln -sf /dev/stdout $SPARK_MASTER_LOG/spark-master.out

/spark/bin/spark-class org.apache.spark.deploy.master.Master \
    --ip $SPARK_LOCAL_IP \
    --port $SPARK_MASTER_PORT \
    --webui-port $SPARK_MASTER_WEBUI_PORT >> $SPARK_MASTER_LOG/spark-master.out