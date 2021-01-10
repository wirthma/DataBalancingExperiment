package cz.cuni.mff.dbe.simulator;

import cz.cuni.mff.dbe.databal.DataBalancer;
import cz.cuni.mff.dbe.datasimulator.DataSimulator;
import cz.cuni.mff.dbe.loadsimulator.LoadSimulator;
import cz.cuni.mff.dbe.nodecountsimulator.NodeCountSimulator;

/**
 * Simulates execution of a given data balancer over a model of a distributed system.
 */
public final class Simulator {
    public Simulator(
            DataBalancer dataBalancer,
            DataSimulator dataSimulator,
            LoadSimulator loadSimulator,
            NodeCountSimulator nodeCountSimulator
    ) {
        this.dataBalancer = dataBalancer;
        this.dataSimulator = dataSimulator;
        this.loadSimulator = loadSimulator;
        this.nodeCountSimulator = nodeCountSimulator;
    }

    /**
     * Executes initialization of data balancing.
     */
    public void simulateInit() {
        dataBalancer.simulateInit();
    }

    /**
     * Executes system node count changes, data distribution changes, data balancing changes and load distribution
     * changes.
     */
    public void simulateIteration() {
        dataBalancer.updateNodeCount(nodeCountSimulator);
        dataBalancer.updateDataDistribution(dataSimulator);
        dataBalancer.simulateIteration(iterationNumber);
        dataBalancer.updateLoadDistribution(loadSimulator);

        ++iterationNumber;
    }

    private final DataBalancer dataBalancer;

    private final DataSimulator dataSimulator;

    private final LoadSimulator loadSimulator;

    private final NodeCountSimulator nodeCountSimulator;

    /**
     * Number of the current iteration.
     */
    private int iterationNumber = 1;
}
