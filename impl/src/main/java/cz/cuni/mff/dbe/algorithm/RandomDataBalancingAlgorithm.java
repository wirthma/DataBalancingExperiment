package cz.cuni.mff.dbe.algorithm;

import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.DataItem;
import cz.cuni.mff.dbe.model.Model;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.util.data.DataDistributionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
     * @param rebalancingIteration Determines number of iterations after which all data are rebalanced in the periodic
     *                              data rebalancing. Values less than 1 are overridden to 1.
     * @param seed The seed for the pseudo-random generator.
     */
    public RandomDataBalancingAlgorithm(
            @Value("${databalancingalgorithm.random.rebalancingIteration}") int rebalancingIteration,
            @Value("${databalancingalgorithm.random.seed}") int seed
    ) {
        this.rebalancingIteration = rebalancingIteration > 1 ? rebalancingIteration : 1;

        random = new Random(seed);
    }

    @Override
    public DataDistributionChange runInit(Model model) {
        return rebalanceIfRebalancingIteration(0, model);
    }

    @Override
    public DataDistributionChange runIteration(int iterationNumber, Model model) {
        return rebalanceIfRebalancingIteration(iterationNumber, model);
    }

    /**
     * Runs {@link #rebalance(Model)} on every {@link #rebalancingIteration} iteration.
     *
     * @param iterationNumber Number of the current iteration.
     */
    private DataDistributionChange rebalanceIfRebalancingIteration(int iterationNumber, Model model) {
        DataDistributionChange dataDistributionChange;
        if (iterationNumber % rebalancingIteration == 0) {
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
                Node newNode = model.getNodes().getNth(random.nextInt(model.getNodes().size()));
                DataDistributionUtils.addToMap(newNode, item, createdItems);
            }
        }

        return new DataDistributionChange(createdItems, removedItems);
    }

    /**
     * Determines number of iterations after which all data are rebalanced in the periodic data rebalancing.
     */
    private final int rebalancingIteration;

    private final Random random;
}
