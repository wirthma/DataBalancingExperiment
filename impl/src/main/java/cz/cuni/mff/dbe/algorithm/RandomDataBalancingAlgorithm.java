package cz.cuni.mff.dbe.algorithm;

import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.DataItem;
import cz.cuni.mff.dbe.model.Model;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.util.data.DataDistributionUtils;
import cz.cuni.mff.dbe.util.node.NodeGen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A {@link DataBalancingAlgorithm} that randomly rebalances the data every given number of iterations.
 *
 * The data are also being rebalanced during the algorithms initialization.
 */
@Component
@ConditionalOnProperty(name = "databalancingalgorithm", havingValue = "random")
public final class RandomDataBalancingAlgorithm implements DataBalancingAlgorithm {
    /**
     * @param rebalancingIterations Determines number of iterations after which all data are rebalanced in the periodic
     *                              data rebalancing. Values less than 1 are overridden to 1.
     * @param seed The seed for the pseudo-random generator.
     */
    public RandomDataBalancingAlgorithm(
            @Value("${databalancingalgorithm.random.rebalancingIterations}") int rebalancingIterations,
            @Value("${databalancingalgorithm.random.seed}") int seed
    ) {
        if (rebalancingIterations > 1) {
            this.rebalancingIterations = rebalancingIterations;
        } else {
            this.rebalancingIterations = 1;
        }

        random = new Random(seed);
    }

    @Override
    public DataDistributionChange runInit(Model model) {
        return doAlgorithmLogic(0, model);
    }

    @Override
    public DataDistributionChange runIteration(int iterationNumber, Model model) {
        return doAlgorithmLogic(iterationNumber, model);
    }

    /**
     * Encapsulates the whole algorithm's logic.
     *
     * @param iterationNumber Number of the current iteration.
     */
    private DataDistributionChange doAlgorithmLogic(int iterationNumber, Model model) {
        DataDistributionChange dataDistributionChange;
        if (iterationNumber % rebalancingIterations == 0) {
            dataDistributionChange = rebalance(model);
        } else {
            dataDistributionChange = new DataDistributionChange();
        }

        return dataDistributionChange;
    }

    /**
     * Performs the random rebalancing.
     */
    private DataDistributionChange rebalance(Model model) {
        Map<Node, List<DataItem>> createdItems = new HashMap<>();
        Map<Node, List<DataItem>> removedItems = new HashMap<>();

        for (Map.Entry<Node, List<DataItem>> nodeItems : model.getDataDistribution().getNodeToDataMap().entrySet()) {
            Node oldNode = nodeItems.getKey();
            List<DataItem> items = nodeItems.getValue();

            DataDistributionUtils.addToMap(oldNode, items, removedItems);

            for (DataItem item : items) {
                Node newNode = NodeGen.getNth(random.nextInt(model.getNodeCount()));
                DataDistributionUtils.addToMap(newNode, item, createdItems);
            }
        }

        return new DataDistributionChange(createdItems, removedItems);
    }

    /**
     * Determines number of iterations after which all data are rebalanced in the periodic data rebalancing.
     */
    private final int rebalancingIterations;

    private final Random random;
}
