package cz.cuni.mff.dbe.datasimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.DataItem;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.util.data.DataItemGen;
import cz.cuni.mff.dbe.util.node.NodeGen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link DataSimulator} that adds the given number of data items into the system's first node and then does not
 * change the data anymore.
 */
public final class StableDataSimulator implements DataSimulator {
    public StableDataSimulator(int dataItemCount) {
        this.dataItemCount = dataItemCount;
        areDataItemsAdded = false;
    }

    @Override
    public DataDistributionChange nextDataDistribution(
            int iterationNumber,
            DataDistribution dataDistribution,
            int nodeCount
    ) {
        if (nodeCount <= 0) {
            return new DataDistributionChange();
        }

        if (!areDataItemsAdded) {
            areDataItemsAdded = true;

            Map<Node, List<DataItem>> createdItems = new HashMap<>();
            createdItems.put(NodeGen.getNth(0), DataItemGen.generateList(dataItemCount));
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
