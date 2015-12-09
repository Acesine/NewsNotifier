#! /bin/bash

PWD=`pwd`
JAR_PATH=$PWD/build/bin/NewsNotifier.jar

ant

java -jar $JAR_PATH \
     -server \
     -Xms16m \
     -Xmx64m \
     -XX:+UseParallelOldGC