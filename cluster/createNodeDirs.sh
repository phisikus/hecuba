#!/bin/bash
if [ ! -d apache-cassandra ]
then
	echo "apache-cassandra directory is missing"
	exit -1
fi

if [ $# -lt 1 ]
then
	echo "use number of nodes as argument"
	exit -1
fi

if [ $1 -lt 1 ]
then
	echo "use a number greater than 0"
	exit -1
fi

numberOfNodes=$1
rm -fr nodes
mkdir nodes

seeds="192.168.43.10"
for ((k=1; k<numberOfNodes; k++))
do
	seeds=$seeds",192.168.43.$(($k+10))"
done

echo "I will create $numberOfNodes nodes..."

for ((i=0; i<numberOfNodes; i++))
do
	mkdir -p nodes/node_$i/apache-cassandra/
	for j in ./apache-cassandra/*
	do
		ln -s ../../../$j ./nodes/node_$i/$j
	done
	rm ./nodes/node_$i/apache-cassandra/conf
	rm ./nodes/node_$i/apache-cassandra/data
	mkdir ./nodes/node_$i/apache-cassandra/data
	cp -r ./apache-cassandra/conf ./nodes/node_$i/apache-cassandra/

	cat ./apache-cassandra/conf/cassandra.yaml | sed \
	-e "s/Test Cluster/HecubaCluster/g" \
	-e "s/127.0.0.1/$seeds/g" \
	-e "s/localhost/192.168.43.$(($i+10))/g" >  ./nodes/node_$i/apache-cassandra/conf/cassandra.yaml

	echo "Created directory and configuration for node $i"
done


echo "Done."
