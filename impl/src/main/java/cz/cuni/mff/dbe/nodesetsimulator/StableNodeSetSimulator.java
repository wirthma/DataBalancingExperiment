package cz.cuni.mff.dbe.nodesetsimulator;

import cz.cuni.mff.dbe.model.DataDistribution;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.model.NodeSet;
import cz.cuni.mff.dbe.model.NodeSetChange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link NodeSetSimulator} that maintains a stable given count of system nodes.
 */
@Component
@ConditionalOnProperty(name = "nodesetsimulator", havingValue = "stable")
public final class StableNodeSetSimulator implements NodeSetSimulator {
    public StableNodeSetSimulator(
            @Value("${nodesetsimulator.stable.nodeCount}") int nodeCount
    ) {
        this.nodeCount = nodeCount;
    }

    @Override
    public NodeSetChange nextNodeSet(int iterationNumber, NodeSet nodeSet, DataDistribution dataDistribution) {
        if (nodeSet.size() != nodeCount) {
            List<Node> nodes = new ArrayList<>();
            for (int i = 0; i < nodeCount; ++i) {
                nodes.add(new Node(i));
            }
            NodeSetChange nodeSetChange = new NodeSetChange(nodes, new ArrayList<>());

            dataDistribution.update(nodes);

            return nodeSetChange;
        } else {
            return new NodeSetChange();
        }
    }

    private final int nodeCount;
}
