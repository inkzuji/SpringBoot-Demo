#!/bin/sh

java -server -Xms500M -Xmx1024M -Xss512k \
 -XX:+AggressiveOpts -XX:+UseBiasedLocking \
 -XX:+DisableExplicitGC -XX:MaxTenuringThreshold=15 \
 -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:+CMSParallelRemarkEnabled \
 -XX:LargePageSizeInBytes=128m \
 -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly \
 -Dfile.encoding=UTF8 -Dsun.jnu.encoding=UTF8 -Duser.timezone=Asia/Shanghai \
 -Djava.awt.headless=true \
 -Djava.security.egd=file:/dev/./urandom \
 -jar app.jar
