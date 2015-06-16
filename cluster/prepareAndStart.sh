#!/bin/sh
./createNodeDirs.sh 3
./startNodes.sh 3
sleep 3s
./apache-cassandra/bin/cqlsh -f data.cassandra

