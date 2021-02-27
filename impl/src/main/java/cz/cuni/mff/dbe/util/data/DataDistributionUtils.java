package cz.cuni.mff.dbe.util.data;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.DataItem;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.util.metrics.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides common operations related to data distribution.
 */
public final class DataDistributionUtils {
    /**
     * Inserts the given data items to the mapping for the given node inside the given map, correctly handling
     * situations like already existing data item list for that node in the given map, etc.
     */
    public static void addToMap(Node node, List<DataItem> items, Map<Node, List<DataItem>> map) {
        List<DataItem> mapItems = map.get(node);
        if (mapItems == null) {
            map.put(node, items);
        } else {
            mapItems.addAll(items);
        }
    }

    /**
     * Inserts the given data item to the mapping for the given node inside the given map, correctly handling situations
     * like already existing data item list for that node in the given map, etc.
     */
    public static void addToMap(Node node, DataItem item, Map<Node, List<DataItem>> map) {
        List<DataItem> list = new ArrayList<>();
        list.add(item);

        addToMap(node, list, map);
    }

    /**
     * Removes the given data items from the mapping for the given node inside the given map, correctly handling
     * situations like no existing data item list for that node in the given map, etc.
     */
    public static void removeFromMap(Node node, List<DataItem> items, Map<Node, List<DataItem>> map) {
        List<DataItem> mapItems = map.get(node);
        if (mapItems != null) {
            mapItems.removeAll(items);
        }
    }

    /**
     * Collects number of data items at individual nodes as metrics.
     *
     * @param dataDistribution The data distribution used.
     * @param iterationNumber The number of the current iteration.
     * @param prefix Prefix used for the metrics name.
     */
    public static void collectNodeSizeMetrics(DataDistribution dataDistribution, int iterationNumber, String prefix) {
        dataDistribution.getNodeToDataMap().forEach(
                (Node node, List <DataItem> dataItems) -> Metrics.record(
                        iterationNumber,
                        prefix + ".node" + node.getId(),
                        dataItems.size()
                )
        );
    }

    public static int countAllDataItems(DataDistribution dataDistribution) {
        int count = 0;

        for (Map.Entry<Node, List<DataItem>> e : dataDistribution.getNodeToDataMap().entrySet()) {
            count += e.getValue().size();
        }

        return count;
    }

    /**
     * Collects created and removed data items in the given {@link DataDistributionChange} as metrics.
     *
     * @param iterationNumber The number of the current iteration.
     * @param prefix Prefix used for the metrics name.
     */
    public static void collectChurnMetrics(
            DataDistributionChange dataDistributionChange,
            int iterationNumber,
            String prefix
    ) {
        long createdDataItemsCount = dataDistributionChange.getCreatedItems().values().stream().flatMap(
                (List<DataItem> dataItemList) -> dataItemList.stream()
        ).count();
        Metrics.record(iterationNumber, prefix + ".created", (int) createdDataItemsCount);

        long removedDataItemsCount = dataDistributionChange.getRemovedItems().values().stream().flatMap(
                (List<DataItem> dataItemList) -> dataItemList.stream()
        ).count();
        Metrics.record(iterationNumber, prefix + ".removed", (int) removedDataItemsCount);
    }
}
