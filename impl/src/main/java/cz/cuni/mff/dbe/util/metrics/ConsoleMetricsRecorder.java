package cz.cuni.mff.dbe.util.metrics;

/**
 * {@link MetricsRecorder} that writes metrics record on the standard output.
 */
public final class ConsoleMetricsRecorder implements MetricsRecorder {
    @Override
    public void record(String metricName, Timestamp timestamp, int value) {
        System.out.println(metricName + " >>> " + timestamp + " : " + value);
    }
}
