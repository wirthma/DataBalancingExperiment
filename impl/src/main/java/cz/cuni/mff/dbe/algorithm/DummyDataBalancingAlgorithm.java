package cz.cuni.mff.dbe.algorithm;

import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.DataItem;
import cz.cuni.mff.dbe.model.Model;
import cz.cuni.mff.dbe.model.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link DataBalancingAlgorithm} that always redistributes all data items to node 0.
 */
public final class DummyDataBalancingAlgorithm implements DataBalancingAlgorithm {
    @Override
    public DataDistributionChange runInit(Model model) {
        return runRebalancing(model);
    }

    @Override
    public DataDistributionChange runIteration(Model model) {
        return runRebalancing(model);
    }

    private DataDistributionChange runRebalancing(Model model) {
        Map<Node, List<DataItem>> createdItems = new HashMap<>();
        Map<Node, List<DataItem>> removedItems = new HashMap<>();

        for (Map.Entry<Node, List<DataItem>> nodeItems : model.getDataDistribution().getNodeToDataMap().entrySet()) {
            Node node = nodeItems.getKey();
            List<DataItem> items = nodeItems.getValue();

            if (node.getId() != 0 && items.size() > 0) {
                createdItems.put(new Node(0), items);
                removedItems.put(node, items);
            }
        }

        return new DataDistributionChange(createdItems, removedItems);
    }
}
