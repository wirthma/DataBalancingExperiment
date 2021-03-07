package cz.cuni.mff.dbe.nodecountsimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * A {@link NodeCountSimulator} that maintains a stable given count of system nodes.
 */
@Component
@ConditionalOnProperty(name = "nodecountsimulator", havingValue = "stable")
public final class StableNodeCountSimulator implements NodeCountSimulator {
    public StableNodeCountSimulator(
            @Value("${nodecountsimulator.stable.nodeCount}") int nodeCount
    ) {
        this.nodeCount = nodeCount;
    }

    @Override
    public int nextNodeCount(int iterationNumber, int nodeCount, DataDistribution dataDistribution) {
        dataDistribution.update(this.nodeCount);
        return this.nodeCount;
    }

    private final int nodeCount;
}
