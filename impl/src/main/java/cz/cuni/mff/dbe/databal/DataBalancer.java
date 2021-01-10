package cz.cuni.mff.dbe.databal;

import cz.cuni.mff.dbe.algorithm.DataBalancingAlgorithm;
import cz.cuni.mff.dbe.datasimulator.DataSimulator;
import cz.cuni.mff.dbe.loadsimulator.LoadSimulator;
import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.DataItem;
import cz.cuni.mff.dbe.model.Model;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.nodecountsimulator.NodeCountSimulator;
import cz.cuni.mff.dbe.util.metrics.Metrics;

import java.util.List;
import java.util.Map;

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
     *
     * @param iterationNumber The number of the current iteration.
     */
    public void simulateIteration(int iterationNumber) {
        DataDistributionChange dataDistributionChange = dataBalancingAlgorithm.runIteration(iterationNumber, model);

        model.updateDataDistribution(dataDistributionChange);

        collectMetrics(iterationNumber);
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
     *
     * @param iterationNumber The number of the current iteration.
     */
    private void collectMetrics(int iterationNumber) {
        model.getDataDistribution().getNodeToDataMap().forEach(
                (Node node, List <DataItem> dataItems) -> Metrics.record(
                        iterationNumber,
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
