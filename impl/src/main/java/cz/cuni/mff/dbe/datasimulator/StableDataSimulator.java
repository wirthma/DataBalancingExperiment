package cz.cuni.mff.dbe.datasimulator;

import cz.cuni.mff.dbe.model.*;
import cz.cuni.mff.dbe.util.data.DataItemGen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link DataSimulator} that adds the given number of data items into the system's node with the least ID and
 * then does not change the data anymore.
 */
@Component
@ConditionalOnProperty(name = "datasimulator", havingValue = "stable")
public final class StableDataSimulator implements DataSimulator {
    public StableDataSimulator(
            @Value("${datasimulator.stable.dataitemcount}") int dataItemCount
    ) {
        this.dataItemCount = dataItemCount;
        areDataItemsAdded = false;
    }

    @Override
    public DataDistributionChange nextDataDistribution(
            int iterationNumber,
            DataDistribution dataDistribution,
            NodeSet nodes
    ) {
        if (nodes.isEmpty()) {
            return new DataDistributionChange();
        }

        if (!areDataItemsAdded) {
            areDataItemsAdded = true;

            Map<Node, List<DataItem>> createdItems = new HashMap<>();
            createdItems.put(nodes.getLeast(), DataItemGen.generateList(dataItemCount));
            return new DataDistributionChange(createdItems, new HashMap<>());
        } else {
            return new DataDistributionChange();
        }
    }

    /**
     * Number of data items that should be in the system.
     */
    private int dataItemCount;

    /**
     * True if the data items have been already added; otherwise false.
     */
    private boolean areDataItemsAdded;
}
