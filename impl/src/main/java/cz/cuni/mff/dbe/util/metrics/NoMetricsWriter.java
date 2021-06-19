package cz.cuni.mff.dbe.util.metrics;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * A {@link MetricsWriter} that throws away any input.
 */
@Component
@ConditionalOnProperty(name = "metricswriter", havingValue = "no")
public final class NoMetricsWriter implements MetricsWriter {
    @Override
    public void record(String metricName, Timestamp timestamp, int value) {
        // empty / no action
    }
}
