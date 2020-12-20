package cz.cuni.mff.dbe.util.metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Helps to collect metrics.
 */
public final class Metrics {
    public static void setMetricsRecorder(MetricsRecorder metricsRecorder) {
        Metrics.metricsRecorder = metricsRecorder;
    }

    public static void record(String metricName, int value) {
        Timestamp timestamp = new Timestamp(getNextIterationNumber(metricName));

        metricsRecorder.record(metricName, timestamp, value);
    }

    /**
     * Returns the next iteration number for the given metric.
     *
     * It also updates the next iteration number for the next call.
     */
    private static int getNextIterationNumber(String metricName) {
        Integer nextIterationNumber = metricsIterationNumbers.get(metricName);
        if (nextIterationNumber == null) {
            nextIterationNumber = 0;
        }

        metricsIterationNumbers.put(metricName, nextIterationNumber + 1);

        return nextIterationNumber;
    }

    /**
     * Stores a next-to-use iteration number for a metric.
     */
    private static Map<String, Integer> metricsIterationNumbers = new HashMap<>();

    private static MetricsRecorder metricsRecorder = new NoMetricsRecorder();
}
