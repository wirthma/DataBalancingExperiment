package cz.cuni.mff.dbe.algorithm;

import cz.cuni.mff.dbe.model.*;
import cz.cuni.mff.dbe.util.data.DataDistributionUtils;
import cz.cuni.mff.dbe.util.node.NodeGen;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link DataBalancingAlgorithm} that always divides the data items onto the nodes uniformly using the modulo
 * operation.
 */
@Component
@ConditionalOnProperty(name = "databalancingalgorithm", havingValue = "roundrobin")
public final class RoundRobinDataBalancingAlgorithm implements DataBalancingAlgorithm {
    @Override
    public DataDistributionChange runInit(Model model) {
        return rebalance(model);
    }

    @Override
    public DataDistributionChange runIteration(int iterationNumber, Model model) {
        return rebalance(model);
    }

    private DataDistributionChange rebalance(Model model) {
        Map<Node, List<DataItem>> createdItems = new HashMap<>();
        Map<Node, List<DataItem>> removedItems = new HashMap<>();

        for (Map.Entry<Node, List<DataItem>> e : model.getDataDistribution().getNodeToDataMap().entrySet()) {
            Node oldNode = e.getKey();
            e.getValue().forEach(
                    (DataItem item) -> {
                        Node newNode = NodeGen.getNth(item.getId() % model.getNodeCount());
                        if (!oldNode.equals(newNode)) {
                            DataDistributionUtils.addToMap(oldNode, item, removedItems);
                            DataDistributionUtils.addToMap(newNode, item, createdItems);
                        }
                    }
            );
        }

        return new DataDistributionChange(createdItems, removedItems);
    }
}
