package cz.cuni.mff.dbe.simulator;

import cz.cuni.mff.dbe.databal.DataBalancer;
import cz.cuni.mff.dbe.datasimulator.DataSimulator;
import cz.cuni.mff.dbe.loadsimulator.LoadSimulator;
import cz.cuni.mff.dbe.nodesetsimulator.NodeSetSimulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Simulates execution of a given data balancer over a model of a distributed system.
 */
@Component
public final class Simulator {
    /**
     * Executes initialization of data balancing.
     */
    public void simulateInit() {
        dataBalancer.simulateInit();
    }

    /**
     * Executes system node set changes, data distribution changes, data balancing changes and load distribution
     * changes.
     */
    public void simulateIteration() {
        dataBalancer.updateNodeSet(nodeSetSimulator, iterationNumber);
        dataBalancer.updateDataDistribution(dataSimulator, iterationNumber);
        dataBalancer.simulateIteration(iterationNumber);
        dataBalancer.updateLoadDistribution(loadSimulator, iterationNumber);

        ++iterationNumber;
    }

    @Autowired
    private DataBalancer dataBalancer;

    @Autowired
    private DataSimulator dataSimulator;

    @Autowired
    private LoadSimulator loadSimulator;

    @Autowired
    private NodeSetSimulator nodeSetSimulator;

    /**
     * Number of the current iteration.
     */
    private int iterationNumber = 1;
}
