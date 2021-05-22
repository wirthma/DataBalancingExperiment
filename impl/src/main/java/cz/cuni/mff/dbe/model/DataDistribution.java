package cz.cuni.mff.dbe.model;

import cz.cuni.mff.dbe.util.data.DataDistributionUtils;

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
     * Checks the given nodes in the data distribution and creates empty data mappings for them if
     * they do not have any.
     */
    public void update(Collection<Node> nodes) {
        nodes.forEach((Node node) -> {
            if (!nodeToDataMap.containsKey(node)) {
                nodeToDataMap.put(node, new ArrayList<>());
            }
        });
    }

    private final Map<Node, List<DataItem>> nodeToDataMap = new HashMap<>();
}
