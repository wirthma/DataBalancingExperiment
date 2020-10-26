package cz.cuni.mff.dbe.util;

/**
 * Helps to collect metrics.
 */
public class Metrics {
    /**
     * Records next value for the metric of the given name.
     */
    public static void record(String metricName, int value) {
        // TODO
        System.out.println(metricName + " = " + value);
    }
}
