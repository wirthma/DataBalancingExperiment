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
 * A {@link DataBalancingAlgorithm} that always divides the data items onto the nodes so that the first N nodes
 * (with respect to their ID) are on the first node, the second N nodes are on the second node and so on.
 */
@Component
@ConditionalOnProperty(name = "databalancingalgorithm", havingValue = "uniform")
public final class UniformDataBalancingAlgorithm implements DataBalancingAlgorithm {
    @Override
    public DataDistributionChange runInit(Model model) {
        return runRebalancing(model);
    }

    @Override
    public DataDistributionChange runIteration(int iterationNumber, Model model) {
        return runRebalancing(model);
    }

    private DataDistributionChange runRebalancing(Model model) {
        if (model.getNodeCount() <= 0) {
            return new DataDistributionChange();
        }

        int dataItemCount = DataDistributionUtils.countAllDataItems(model.getDataDistribution());
        int dataItemCountPerNode = dataItemCount / model.getNodeCount();

        Map<Node, List<DataItem>> createdItems = new HashMap<>();
        Map<Node, List<DataItem>> removedItems = new HashMap<>();

        for (Map.Entry<Node, List<DataItem>> e : model.getDataDistribution().getNodeToDataMap().entrySet()) {
            Node oldNode = e.getKey();
            e.getValue().forEach(
                    (DataItem item) -> {
                        Node newNode = NodeGen.getNth(item.getId() / dataItemCountPerNode);
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
