package cz.cuni.mff.dbe.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents (active) nodes in the system.
 */
public final class NodeSet {
    /**
     * @return All stored {@link Node nodes}.
     */
    public Collection<Node> getAll() {
        return nodes.values();
    }

    /**
     * @return The {@link Node} with the least ID greater or equal than the given ID or null if no such exists.
     */
    public Node getNext(int id) {
        return entryToNode(nodes.ceilingEntry(id));
    }

    /**
     * @return The {@link Node} with the least ID greater or equal than the given ID or null if no such exists.
     */
    public Node getPrevious(int id) {
        return entryToNode(nodes.floorEntry(id));
    }

    /**
     * @return The {@link Node} with the least ID greater or equal than the given ID; or, if none such exists,
     * the node with the least ID; or, if there are no nodes, null.
     */
    public Node getNextOnRing(int id) {
        Node nextNode = getNext(id);
        if (nextNode != null) {
            return nextNode;
        } else {
            return getLeast();
        }
    }

    /**
     * @return The {@link Node} with the greatest ID less or equal than the given ID; or, if none such exists,
     * the node with the least ID; or, if there are no nodes, null.
     */
    public Node getPreviousOnRing(int id) {
        Node previousNode = getPrevious(id);
        if (previousNode != null) {
            return previousNode;
        } else {
            return getGreatest();
        }
    }

    /**
     * @return The {@link Node} with the least ID; or null if there are no nodes.
     */
    public Node getLeast() {
        return getNext(Integer.MIN_VALUE);
    }

    /**
     * @return The {@link Node} with the least ID; or null if there are no nodes.
     */
    public Node getGreatest() {
        return getPrevious(Integer.MAX_VALUE);
    }

    /**
     * @return {@link Node} with the given ID or null if none such exists.
     */
    public Node get(int id) {
        return nodes.get(id);
    }

    /**
     * @return The n-th {@link Node}, ordered by the ID ascending, or null if there is no such node.
     */
    public Node getNth(int n) {
        if (n < 0 || n >= size()) {
            return null;
        }

        int nthKey = 0;
        Iterator<Integer> it = nodes.navigableKeySet().iterator();
        for (int i = 0; i <= n; ++i) {
            nthKey = it.next();
        }
        return nodes.get(nthKey);
    }

    /**
     * @return Number of nodes in the {@link NodeSet}.
     */
    public int size() {
        return nodes.size();
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public void update(NodeSetChange nodeSetChange) {
        nodeSetChange.getRemovedNodes().forEach((Node node) -> nodes.remove(node.getId()));
        nodeSetChange.getCreatedNodes().forEach((Node node) -> nodes.put(node.getId(), node));
    }

    /**
     * @return The {@link Node} from the given {@link Map.Entry} or null on null.
     */
    private Node entryToNode(Map.Entry<Integer, Node> entry) {
        return entry == null ? null : entry.getValue();
    }

    /**
     * Map of IDs to corresponding {@link Node nodes}.
     */
    private TreeMap<Integer, Node> nodes = new TreeMap<>();
}
