package cz.cuni.mff.dbe.algorithm;

import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.Model;

/**
 * Defines an algorithm for data balancing.
 */
public interface DataBalancingAlgorithm {
    /**
     * Runs an initialization routine of the algorithm to do any preparation before periodic iterations
     * of normal operation.
     *
     * @param model The data balancer's model of the system when calling this method. It will not be modified.
     * @return Changes to be done in the data distribution in the system.
     */
    DataDistributionChange runInit(Model model);

    /**
     * Runs a single iteration of normal operation.
     *
     * @param model The data balancer's model of the system when calling this method. It will not be modified.
     * @return Changes to be done in the data distribution in the system.
     */
    DataDistributionChange runIteration(Model model);
}
