package cz.cuni.mff.dbe.util.metrics;

/**
 * Provides some kind of metrics recording.
 */
public interface MetricsRecorder {
    void record(String metricName, Timestamp timestamp, int value);
}
