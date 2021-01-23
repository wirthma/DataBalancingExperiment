package cz.cuni.mff.dbe.model;

import cz.cuni.mff.dbe.datasimulator.DataSimulator;
import cz.cuni.mff.dbe.loadsimulator.LoadSimulator;
import cz.cuni.mff.dbe.nodecountsimulator.NodeCountSimulator;

/**
 * Captures the data and load distribution in the system, as a model for the data balancer.
 */
public final class Model {
    /**
     * @return Do not modify.
     */
    public DataDistribution getDataDistribution() {
        return dataDistribution;
    }

    /**
     * @param iterationNumber Number of the current iteration.
     */
    public void updateDataDistribution(DataSimulator dataSimulator, int iterationNumber) {
        DataDistributionChange dataDistributionChange = dataSimulator.nextDataDistribution(
                iterationNumber,
                dataDistribution,
                nodeCount
        );

        dataDistribution.update(dataDistributionChange);
    }

    public void updateDataDistribution(DataDistributionChange dataDistributionChange) {
        dataDistribution.update(dataDistributionChange);
    }

    /**
     * @return Do not modify.
     */
    public LoadDistribution getLoadDistribution() {
        return loadDistribution;
    }

    /**
     * @param iterationNumber Number of the current iteration.
     */
    public void updateLoadDistribution(LoadSimulator loadSimulator, int iterationNumber) {
        LoadDistributionChange loadDistributionChange = loadSimulator.nextLoadDistribution(
                iterationNumber,
                loadDistribution,
                dataDistribution
        );

        loadDistribution.update(loadDistributionChange);
    }

    public void updateLoadDistribution(LoadDistributionChange loadDistributionChange) {
        loadDistribution.update(loadDistributionChange);
    }

    public int getNodeCount() {
        return nodeCount;
    }

    /**
     * @param iterationNumber Number of the current iteration.
     */
    public void updateNodeCount(int iterationNumber, NodeCountSimulator nodeCountSimulator) {
        nodeCount = nodeCountSimulator.nextNodeCount(iterationNumber, nodeCount, dataDistribution);
    }

    public void updateNodeCount(int nodeCount) {
        this.nodeCount = nodeCount;
    }

    private int nodeCount = 0;

    private final DataDistribution dataDistribution = new DataDistribution();

    private final LoadDistribution loadDistribution = new LoadDistribution();
}
