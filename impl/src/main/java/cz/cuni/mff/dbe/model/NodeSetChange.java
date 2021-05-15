package cz.cuni.mff.dbe.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines change in {@link NodeSet}.
 */
public final class NodeSetChange {
    /**
     * Creates an empty {@link NodeSetChange} representing "no action".
     */
    public NodeSetChange() {
        createdNodes = new ArrayList<>();
        removedNodes = new ArrayList<>();
    }

    public NodeSetChange(List<Node> createdNodes, List<Node> removedNodes) {
        this.createdNodes = createdNodes;
        this.removedNodes = removedNodes;
    }

    public List<Node> getCreatedNodes() {
        return createdNodes;
    }

    public List<Node> getRemovedNodes() {
        return removedNodes;
    }

    private final List<Node> createdNodes;

    private final List<Node> removedNodes;
}
