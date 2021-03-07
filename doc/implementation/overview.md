# High-Level Implementation Overview

This document is organized as follows. First, make a quick note about existence of simulation iterations.
Then we describe the main objects used in our implementation, and the system model. With that over, the
following section is able to present steps of an individual simulation iteration. And finally, the last
section is dedicated to concurrency in our implementation.

## Simulation Iterations

The simulation is composed of subsequent iterations. Individual steps inside the iteration are discussed later
in this document. At the end of each iteration, the system and the data are in a consistent state.

All metrics are collected with respect to an iteration number, not a real time.

## Object Design

The whole simulation is driven by the [`Simulator`][Simulator] object. It requires four strategies as parameters of
the distributed system simulation:
- [`DataBalancingAlgorithm`][DataBalancingAlgorithm] which defines how the data is being balanced over the simulation
  iterations.
- [`DataSimulator`][DataSimulator] which specifies how data are being manipulated by create/remove requests from
  outside the system, over individual simulation iterations.
- [`LoadSimulator`][LoadSimulator] which determines how the data are being queried from outside the system, i.e.
  what the load on individual data items is. Again, over individual simulation iterations.
- [`NodeCountSimulator`][NodeCountSimulator] which defines how nodes are being added to or removed from the system,
  over the simulation iterations.

The data balancer component is represented by the [`DataBalancer`][DataBalancer] object and it processes instructions
from the above-described strategies by updating correspondingly a model of the system.

## System Model

The model of the system is represented by the [`Model`][Model] object.

It comprises basically of three parameters:
- The map of nodes to there-stored data items.
- The map of data items to their load.
- The number of system nodes.

## Iteration Steps

Each iteration of the simulation follows four steps executed in the following order:
1. Update of the number of system nodes (i.e. the system nodes are added or removed).
2. Update of the data in the system (i.e. processing of requests of data items creations or removals from outside
   the system).
3. Data balancing algorithm's iteration.
4. Update of the load in the system (i.e. update of load on individual dat items caused by queries from outside
   the system).

## Concurrency

The whole simulation is run in a single thread to help guarantee that the found results are valid. Multi-threaded
environment is more error-prone.

This negatively impacts the experiment's performance, but we traded it for correctness of the results and easiness
of the implementation.


[Simulator]: ../../impl/src/main/java/cz/cuni/mff/dbe/simulator/Simulator.java
[DataBalancingAlgorithm]: ../../impl/src/main/java/cz/cuni/mff/dbe/algorithm/DataBalancingAlgorithm.java
[DataSimulator]: ../../impl/src/main/java/cz/cuni/mff/dbe/datasimulator/DataSimulator.java
[LoadSimulator]: ../../impl/src/main/java/cz/cuni/mff/dbe/loadsimulator/LoadSimulator.java
[NodeCountSimulator]: ../../impl/src/main/java/cz/cuni/mff/dbe/nodecountsimulator/NodeCountSimulator.java
[Model]: ../../impl/src/main/java/cz/cuni/mff/dbe/model/Model.java
[DataBalancer]: ../../impl/src/main/java/cz/cuni/mff/dbe/databal/DataBalancer.java
