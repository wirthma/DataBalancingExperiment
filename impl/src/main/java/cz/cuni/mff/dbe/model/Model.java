package cz.cuni.mff.dbe.model;

import cz.cuni.mff.dbe.datasimulator.DataSimulator;
import cz.cuni.mff.dbe.loadsimulator.LoadSimulator;
import cz.cuni.mff.dbe.nodesetsimulator.NodeSetSimulator;

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
                nodes
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

    public NodeSet getNodes() {
        return nodes;
    }

    /**
     * @param iterationNumber Number of the current iteration.
     */
    public void updateNodes(int iterationNumber, NodeSetSimulator nodeSetSimulator) {
        NodeSetChange nodeSetChange = nodeSetSimulator.nextNodeSet(iterationNumber, nodes, dataDistribution);

        nodes.update(nodeSetChange);
    }

    public void updateNodes(NodeSetChange nodeSetChange) {
        nodes.update(nodeSetChange);
    }

    private NodeSet nodes = new NodeSet();

    private final DataDistribution dataDistribution = new DataDistribution();

    private final LoadDistribution loadDistribution = new LoadDistribution();
}
