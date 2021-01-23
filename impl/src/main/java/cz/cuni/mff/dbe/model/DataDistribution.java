package cz.cuni.mff.dbe.model;

import cz.cuni.mff.dbe.util.data.DataDistributionUtils;
import cz.cuni.mff.dbe.util.node.NodeGen;

import java.util.*;

/**
 * Stores the current distribution of data items across individual nodes in the system.
 */
public final class DataDistribution {
    /**
     * @return Do not modify.
     */
    public Map<Node, List<DataItem>> getNodeToDataMap() {
        return nodeToDataMap;
    }

    public void update(DataDistributionChange dataDistributionChange) {
        dataDistributionChange.getRemovedItems().forEach(
                (Node node, List<DataItem> items) -> {
                    DataDistributionUtils.removeFromMap(node, items, nodeToDataMap);
                }
        );

        dataDistributionChange.getCreatedItems().forEach(
                (Node node, List<DataItem> items) -> {
                    DataDistributionUtils.addToMap(node, items, nodeToDataMap);
                }
        );
    }

    /**
     * Checks first given number of nodes in the data distribution and creates empty data mappings for them if
     * they do not have any.
     */
    public void update(int nodeCount) {
        for (int i = 0; i < nodeCount; ++i) {
            Node node = NodeGen.getNth(i);

            if (!nodeToDataMap.containsKey(node)) {
                nodeToDataMap.put(node, new ArrayList<>());
            }
        }
    }

    private final Map<Node, List<DataItem>> nodeToDataMap = new HashMap<>();
}
