#!/bin/bash
/spark/bin/spark-submit --master $SPARK_MASTER \
--class $JAR_CLASS $JAR $ARGS