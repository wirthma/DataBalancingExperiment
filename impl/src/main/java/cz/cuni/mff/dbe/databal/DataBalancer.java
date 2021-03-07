package cz.cuni.mff.dbe.databal;

import cz.cuni.mff.dbe.algorithm.DataBalancingAlgorithm;
import cz.cuni.mff.dbe.datasimulator.DataSimulator;
import cz.cuni.mff.dbe.loadsimulator.LoadSimulator;
import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.Model;
import cz.cuni.mff.dbe.nodecountsimulator.NodeCountSimulator;
import cz.cuni.mff.dbe.util.data.DataDistributionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Simulates the data balancer as a whole.
 */
@Component
public final class DataBalancer {
    public DataBalancer(@Autowired DataBalancingAlgorithm dataBalancingAlgorithm) {
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
        DataDistributionUtils.collectNodeSizeMetrics(
                model.getDataDistribution(),
                iterationNumber,
                "datadistribution"
        );

        DataDistributionChange dataDistributionChange = dataBalancingAlgorithm.runIteration(iterationNumber, model);

        DataDistributionUtils.collectChurnMetrics(dataDistributionChange, iterationNumber, "dataitemchurn");

        model.updateDataDistribution(dataDistributionChange);
    }

    /**
     * Updates the information about load distribution in the model of the data balancer.
     *
     * @param iterationNumber Number of the current iteration.
     */
    public void updateLoadDistribution(LoadSimulator loadSimulator, int iterationNumber) {
        model.updateLoadDistribution(loadSimulator, iterationNumber);
    }

    /**
     * Updates the information about data distribution in the model of the data balancer.
     *
     * @param iterationNumber Number of the current iteration.
     */
    public void updateDataDistribution(DataSimulator dataSimulator, int iterationNumber) {
        model.updateDataDistribution(dataSimulator, iterationNumber);
    }

    /**
     * Updates the information about the node count in the model of the data balancer.
     *
     * @param iterationNumber Number of the current iteration.
     */
    public void updateNodeCount(NodeCountSimulator nodeCountSimulator, int iterationNumber) {
        model.updateNodeCount(iterationNumber, nodeCountSimulator);
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
