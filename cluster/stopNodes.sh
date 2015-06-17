#!/bin/bash
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
interface=$2
for ((i=0; i<numberOfNodes; i++))
do
	sudo ip addr del 192.168.43.$(($i+10))/24 dev $interface
	screen -X -S cassandra_$i quit
	echo "Stopped node_$i"
done
echo "Done."
