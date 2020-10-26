package cz.cuni.mff.dbe.model;

/**
 * Represents a data node in the system.
 */
public final class Node {
    public Node(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Node && id == ((Node) other).id;
    }

    @Override
    public String toString() {
        return "node[" + id + "]";
    }

    private final int id;
}
