package cz.cuni.mff.dbe.model;

/**
 * Represents a data item stored in the system.
 */
public final class DataItem {
    public DataItem(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof DataItem && id == ((DataItem) other).id;
    }

    @Override
    public String toString() {
        return "item[" + id + "]";
    }

    private final int id;
}
