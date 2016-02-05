# Hecuba

This project is an proof-of-concept implementation of shared objects in a distributed environment. It utilizes Apache Cassandra distributed database as a medium of communication to provide Lamport-like mutual exclusion mechanism. It uses two tables - one that stores shared objects and second one that contains history of requests and responses tagged with IDs to establish parent-child relation between them, since certain rows can be available in different replicas at different times. It's the type of thing that you should not have to do ;-). The source code includes generic library (BasicObjectManager) that can manage objects (SharedObject) and example implementation (managing train tickets).

## Usage
The general idea is that you create multiple Cassandra replicas. In the "cluster" directory you can find some scripts that you can use. The general idea is to create multiple working directories and start each replica on a different IP address. You can use loopback adresses like 127.0.0.1, 127.0.0.2 ... or bind multiple IPs to your "real" NIC to enable connections from other hardware nodes if necessary. Check "cluster/startNodes.sh" and "cluster/createNodeDirs.sh" for inspirations. General workflow is shown in "cluster/prepareAndStart.sh".

You can run tests using Maven:

  mvn clean test

You can run single node using:

  mvn compile exec:java -Dexec.mainClass="pl.poznan.put.cs.dsg.srds.cassandra.Main"

Check config.properties and set "hecuba.numberOfNodes" property according to the number of nodes that you will be using.

## Issues

In general this project's liveness is based on assumption that replicas are being synchronized in a timely manner. By default the algorithm will wait for all the nodes to respond by updating their database replicas so in a highly distributed environment it will be slow and inefficient. The mutual exclusion is provided, but there is no guarantee that retrieved database shared object will be in the newest version possible (also depends on database configuration).

## Authors

Marcin Biernacki (phisikus) & Filip Rachwalak (frach)
