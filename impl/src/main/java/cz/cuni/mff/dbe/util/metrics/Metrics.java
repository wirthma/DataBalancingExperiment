package cz.cuni.mff.dbe.util.metrics;

/**
 * Helps to collect metrics.
 */
public final class Metrics {
    public static void setMetricsRecorder(MetricsRecorder metricsRecorder) {
        Metrics.metricsRecorder = metricsRecorder;
    }

    public static void record(int iterationNumber, String metricName, int value) {
        Timestamp timestamp = new Timestamp(iterationNumber);

        metricsRecorder.record(metricName, timestamp, value);
    }

    private static MetricsRecorder metricsRecorder = new NoMetricsRecorder();
}
