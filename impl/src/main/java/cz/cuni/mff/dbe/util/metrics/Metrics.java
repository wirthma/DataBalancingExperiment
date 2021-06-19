package cz.cuni.mff.dbe.util.metrics;

/**
 * Helps to collect metrics.
 */
public final class Metrics {
    public static void setMetricsWriter(MetricsWriter metricsWriter) {
        Metrics.metricsWriter = metricsWriter;
    }

    public static void record(int iterationNumber, String metricName, int value) {
        Timestamp timestamp = new Timestamp(iterationNumber);

        metricsWriter.record(metricName, timestamp, value);
    }

    private static MetricsWriter metricsWriter = new NoMetricsWriter();
}
