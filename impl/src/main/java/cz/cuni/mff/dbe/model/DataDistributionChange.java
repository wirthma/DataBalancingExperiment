package cz.cuni.mff.dbe.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines a change in data distribution on system nodes.
 */
public final class DataDistributionChange {
    /**
     * Creates an empty {@link DataDistributionChange} representing "no action".
     */
    public DataDistributionChange() {
        createdItems = new HashMap<>();
        removedItems = new HashMap<>();
    }

    /**
     * @param createdItems All items to be created on given system nodes.
     * @param removedItems All items to be removed from given system nodes.
     */
    public DataDistributionChange(Map<Node, List<DataItem>> createdItems, Map<Node, List<DataItem>> removedItems) {
        this.createdItems = createdItems;
        this.removedItems = removedItems;
    }

    /**
     * @return All items to be created on given system nodes. Do not modify.
     */
    public Map<Node, List<DataItem>> getCreatedItems() {
        return createdItems;
    }

    /**
     * @return Items to be removed from given system nodes. Do not modify.
     */
    public Map<Node, List<DataItem>> getRemovedItems() {
        return removedItems;
    }

    /**
     * Stores all items to be created on given system nodes.
     */
    private final Map<Node, List<DataItem>> createdItems;

    /**
     * Stores all items to be removed from given system nodes.
     */
    private final Map<Node, List<DataItem>> removedItems;
}
