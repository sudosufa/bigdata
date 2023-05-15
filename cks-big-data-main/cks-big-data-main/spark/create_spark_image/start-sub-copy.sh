#!/bin/bash
/spark/bin/spark-submit --master $SPARK_MASTER --class $JAR_CLASS \
--deploy-mode $DEPLOY_MODE \
--supervise $JAR $ARGS