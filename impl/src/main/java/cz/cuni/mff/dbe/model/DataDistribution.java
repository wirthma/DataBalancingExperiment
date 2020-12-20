package cz.cuni.mff.dbe.model;

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
        dataDistributionChange.getCreatedItems().forEach(
                (Node node, List<DataItem> dataItems) -> {
                    if (!nodeToDataMap.containsKey(node)) {
                        nodeToDataMap.put(node, new ArrayList<>());
                    }

                    nodeToDataMap.get(node).addAll(dataItems);
                }
        );

        dataDistributionChange.getRemovedItems().forEach(
                (Node node, List<DataItem> dataItems) -> nodeToDataMap.get(node).removeAll(dataItems)
        );
    }

    private final Map<Node, List<DataItem>> nodeToDataMap = new HashMap<>();
}
