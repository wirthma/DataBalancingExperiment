package cz.cuni.mff.dbe.algorithm;

import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.DataItem;
import cz.cuni.mff.dbe.model.Model;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.util.data.DataDistributionUtils;
import cz.cuni.mff.dbe.util.node.NodeGen;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link DataBalancingAlgorithm} that always redistributes all data items to node 0.
 */
@Component
@ConditionalOnProperty(name = "databalancingalgorithm", havingValue = "onenode")
public final class OneNodeDataBalancingAlgorithm implements DataBalancingAlgorithm {
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

        Node node0 = NodeGen.getNth(0);

        for (Map.Entry<Node, List<DataItem>> nodeItems : model.getDataDistribution().getNodeToDataMap().entrySet()) {
            Node node = nodeItems.getKey();
            List<DataItem> items = nodeItems.getValue();

            if (!node.equals(node0)) {
                DataDistributionUtils.addToMap(node0, items, createdItems);
                DataDistributionUtils.addToMap(node, items, removedItems);
            }
        }

        return new DataDistributionChange(createdItems, removedItems);
    }
}
