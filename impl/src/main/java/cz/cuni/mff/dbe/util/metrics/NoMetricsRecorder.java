package cz.cuni.mff.dbe.util.metrics;

/**
 * A {@link MetricsRecorder} that throws away any input.
 */
public final class NoMetricsRecorder implements MetricsRecorder {
    @Override
    public void record(String metricName, Timestamp timestamp, int value) {
        // empty / no action
    }
}
