#!/bin/bash
if [ $# -lt 2 ]
then
	echo "use number of nodes and interface as argument"
	exit -1
fi

numberOfNodes=$1
interface=$2
for ((i=0; i<numberOfNodes; i++))
do
	
	sudo ip addr add 192.168.43.$(($i+10))/24 dev $interface
	export CASSANDRA_HOME=./nodes/node_$i/apache-cassandra/
	screen -S cassandra_$i -dm ./nodes/node_$i/apache-cassandra/bin/cassandra -f
	echo "Started node_$i"
done
echo "Done."
