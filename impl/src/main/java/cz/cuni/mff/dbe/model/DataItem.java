package cz.cuni.mff.dbe.model;

/**
 * Represents a data item stored in the system.
 */
public final class DataItem {
    /**
     * @param timestamp The time point when the data item was created.
     */
    public DataItem(int id, int timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    /**
     * Returns the time point when the data item was created.
     */
    public int getTimestamp() {
        return timestamp;
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

    /**
     * The time point when the data item was created.
     */
    private final int timestamp;
}
