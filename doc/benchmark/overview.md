# Benchmark

This documentation describes the benchmarking code of the implementation.

The main idea is that we do not implement a real distributed system for purposes of the experiment. We focus only on
execution of some centralized data balancer component that would handle the decision-making about data distribution in
such a system.

This significantly simplifies the implementation. We do not have to solve distributed communication, data consistency,
performance, etc. We can abstract from these aspects because:
1. We track metrics in iterations where all the data distribution in the system is in a consistent state. This is
   valid as even in an eventual consistency model, there are points of complete data consistency.
2. We do not collect metrics with respect to real clock time but with respect to iteration number.

## High-Level Overview

The whole simulation is run in a single thread to help guarantee that the found results are valid (multi-threaded
environment is considered to be error-prone).

The whole simulation is driven by the [`Simulator`][Simulator] object. It requires four strategies as parameters of
the distributed system simulation:
- [`DataBalancingAlgorithm`][DataBalancingAlgorithm] which defines how the data is being balanced over the simulation
  iterations.
- [`DataSimulator`][DataSimulator] which specifies how data are manipulated by create/remove requests from outside
  the system, over individual simulation iteration.
- [`LoadSimulator`][LoadSimulator] which determines how the data are queried from outside the system, again over
  simulation iterations.
- [`NodeCountSimulator`][NodeCountSimulator] which defines how nodes in the system are deployed and undeployed over
  the simulation iterations.

The data balancer component is represented by the [`DataBalancer`][DataBalancer] object. It processes instructions from
the above-described strategies by updating correspondingly a model of the system. That is represented by the
[`Model`][Model] object.

The model of the distributed system comprises basically of three parameters:
- The map of nodes to there-stored data items.
- The map of data items to their load.
- The number of system nodes.

## Simulation Iteration

Each iteration of the simulation follows four steps executed in the following order:
1. Update of the number of system nodes (i.e. the system nodes are deployed or undeployed).
2. Update of the data in the system (i.e. processing of requests of data items creations or removals from outside
   the system).
3. Data balancing algorithm's iteration.
4. Update of the load in the system (i.e. update of load on individual dat items caused by queries from outside
   the system).
   

[Simulator]: ../../impl/src/main/java/cz/cuni/mff/dbe/simulator/Simulator.java
[DataBalancingAlgorithm]: ../../impl/src/main/java/cz/cuni/mff/dbe/algorithm/DataBalancingAlgorithm.java
[DataSimulator]: ../../impl/src/main/java/cz/cuni/mff/dbe/datasimulator/DataSimulator.java
[LoadSimulator]: ../../impl/src/main/java/cz/cuni/mff/dbe/loadsimulator/LoadSimulator.java
[NodeCountSimulator]: ../../impl/src/main/java/cz/cuni/mff/dbe/nodecountsimulator/NodeCountSimulator.java
[Model]: ../../impl/src/main/java/cz/cuni/mff/dbe/model/Model.java
[DataBalancer]: ../../impl/src/main/java/cz/cuni/mff/dbe/databal/DataBalancer.java
