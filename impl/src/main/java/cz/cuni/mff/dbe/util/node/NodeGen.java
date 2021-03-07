package cz.cuni.mff.dbe.util.node;

import cz.cuni.mff.dbe.model.Node;

/**
 * Generates {@link Node nodes} and provides access to already generated ones.
 *
 * This is a singleton object.
 */
public final class NodeGen {
    /**
     * Returns the n-th node in the system (or generates it if not already existing).
     */
    public static Node getNth(int n) {
        return new Node(n);
    }
}
