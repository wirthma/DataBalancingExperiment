package cz.cuni.mff.dbe.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines a change in load distribution on data items.
 */
public final class LoadDistributionChange {
    /**
     * Creates an empty {@link LoadDistributionChange} representing "no action".
     */
    public LoadDistributionChange() {
        changedLoad = new HashMap<>();
    }

    /**
     * @param changedLoad Load information to be set for given data items.
     */
    public LoadDistributionChange(Map<DataItem, Integer> changedLoad) {
        this.changedLoad = changedLoad;
    }

    /**
     * @return Load information to be set for given data items.
     */
    public Map<DataItem, Integer> getChangedLoad() {
        return changedLoad;
    }

    /**
     * Load information to be set for given data items.
     */
    private final Map<DataItem, Integer> changedLoad;
}
