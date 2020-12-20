package cz.cuni.mff.dbe.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores load on individual data items in the system.
 */
public final class LoadDistribution {
    /**
     * @return Do not modify.
     */
    public Map<DataItem, Integer> getItemToLoadMap() {
        return itemToLoadMap;
    }

    public void update(LoadDistributionChange loadDistributionChange) {
        loadDistributionChange.getChangedLoad().forEach(itemToLoadMap::put);
    }

    private final Map<DataItem, Integer> itemToLoadMap = new HashMap<>();
}
