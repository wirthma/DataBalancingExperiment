package cz.cuni.mff.dbe.util.metrics;

public final class Timestamp {
    public Timestamp(int iterationNumber) {
        timestamp = "" + iterationNumber;
    }

    @Override
    public String toString() {
        return timestamp;
    }

    private String timestamp;
}
