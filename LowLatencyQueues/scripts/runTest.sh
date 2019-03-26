#!/bin/bash
java -server $JVM_OPTS -classpath $WORK_HOME/LOWLATENCY/LowLatencyQueues/target/LowLatencyQueues-1.0-SNAPSHOT.jar org.mk.training.queues.harnessandtorginal.$1 $2
