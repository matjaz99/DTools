#!/bin/bash

# correct file permissions before start
find ./ -name \*.sh -exec chmod 755 {} +

echo Starting DTools...
./server/apache-tomcat-7.0.57/bin/startup.sh

