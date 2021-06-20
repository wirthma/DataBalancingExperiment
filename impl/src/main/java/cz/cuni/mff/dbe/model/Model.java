package cz.cuni.mff.dbe.model;

import cz.cuni.mff.dbe.datasimulator.DataSimulator;
import cz.cuni.mff.dbe.loadsimulator.LoadSimulator;
import cz.cuni.mff.dbe.nodesetsimulator.NodeSetSimulator;
import cz.cuni.mff.dbe.util.ds.TokenRing;

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
                nodeSet
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

    public TokenRing<Integer, Node> getNodeSet() {
        return nodeSet;
    }

    /**
     * @param iterationNumber Number of the current iteration.
     */
    public void updateNodeSet(int iterationNumber, NodeSetSimulator nodeSetSimulator) {
        NodeSetChange nodeSetChange = nodeSetSimulator.nextNodeSet(iterationNumber, nodeSet, dataDistribution);

        updateNodeSet(nodeSetChange);
    }

    public void updateNodeSet(NodeSetChange nodeSetChange) {
        for (Node node : nodeSetChange.getCreatedNodes()) {
            nodeSet.add(node.getId(), node);
        }
        for (Node node : nodeSetChange.getRemovedNodes()) {
            nodeSet.remove(node.getId());
        }
    }

    /**
     * System nodes placed on a ring ordered by their ID.
     * <p>
     * This organization of nodes is advantageous for some of the data balancing algorithms.
     */
    private TokenRing<Integer, Node> nodeSet = new TokenRing<>();

    private final DataDistribution dataDistribution = new DataDistribution();

    private final LoadDistribution loadDistribution = new LoadDistribution();
}
