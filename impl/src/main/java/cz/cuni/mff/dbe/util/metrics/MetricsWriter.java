package cz.cuni.mff.dbe.util.metrics;

/**
 * Provides some kind of metrics writing.
 */
public interface MetricsWriter {
    void record(String metricName, Timestamp timestamp, int value);
}
