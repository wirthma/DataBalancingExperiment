package cz.cuni.mff.dbe.util.metrics;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * {@link MetricsWriter} that writes metrics record on the standard output.
 */
@Component
@ConditionalOnProperty(name = "metricswriter", havingValue = "console")
public final class ConsoleMetricsWriter implements MetricsWriter {
    @Override
    public void record(String metricName, Timestamp timestamp, int value) {
        System.out.println(metricName + " >>> " + timestamp + " : " + value);
    }
}
