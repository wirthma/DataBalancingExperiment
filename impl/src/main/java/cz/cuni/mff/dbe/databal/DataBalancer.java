package cz.cuni.mff.dbe.databal;

import cz.cuni.mff.dbe.algorithm.DataBalancingAlgorithm;
import cz.cuni.mff.dbe.datasimulator.DataSimulator;
import cz.cuni.mff.dbe.loadsimulator.LoadSimulator;
import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.DataItem;
import cz.cuni.mff.dbe.model.Model;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.nodecountsimulator.NodeCountSimulator;
import cz.cuni.mff.dbe.util.Metrics;

import java.util.List;

/**
 * Simulates the data balancer as a whole.
 */
public final class DataBalancer {
    public DataBalancer(DataBalancingAlgorithm dataBalancingAlgorithm) {
        this.dataBalancingAlgorithm = dataBalancingAlgorithm;
    }

    /**
     * Simulates initialization of the data balancer.
     */
    public void simulateInit() {
        DataDistributionChange dataDistributionChange = dataBalancingAlgorithm.runInit(model);

        model.updateDataDistribution(dataDistributionChange);
    }

    /**
     * Simulates one iteration of the data balancing algorithm.
     */
    public void simulateIteration() {
        DataDistributionChange dataDistributionChange = dataBalancingAlgorithm.runIteration(model);

        model.updateDataDistribution(dataDistributionChange);

        collectMetrics();
    }

    /**
     * Updates the information about load distribution in the model of the data balancer.
     */
    public void updateLoadDistribution(LoadSimulator loadSimulator) {
        model.updateLoadDistribution(loadSimulator);
    }

    /**
     * Updates the information about data distribution in the model of the data balancer.
     */
    public void updateDataDistribution(DataSimulator dataSimulator) {
        model.updateDataDistribution(dataSimulator);
    }

    /**
     * Updates the information about the node count in the model of the data balancer.
     */
    public void updateNodeCount(NodeCountSimulator nodeCountSimulator) {
        model.updateNodeCount(nodeCountSimulator);
    }

    /**
     * Collects metrics after an iteration.
     */
    private void collectMetrics() {
        model.getDataDistribution().getNodeToDataMap().forEach(
                (Node node, List <DataItem> dataItems) -> Metrics.record(
                        "model.datadistribution.node" + node.getId() + ".size",
                        dataItems.size()
                )
        );
    }

    /**
     * Model of the system.
     */
    private final Model model = new Model();

    /**
     * The data balancing algorithm that is used in the data balancer.
     */
    private final DataBalancingAlgorithm dataBalancingAlgorithm;
}
