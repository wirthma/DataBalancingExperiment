package cz.cuni.mff.dbe.model;

import cz.cuni.mff.dbe.util.datadistribution.DataDistributionUtils;

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

    private final Map<Node, List<DataItem>> nodeToDataMap = new HashMap<>();
}
