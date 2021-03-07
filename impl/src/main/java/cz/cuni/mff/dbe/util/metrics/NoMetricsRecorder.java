package cz.cuni.mff.dbe.util.metrics;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * A {@link MetricsRecorder} that throws away any input.
 */
@Component
@ConditionalOnProperty(name = "metricsrecorder", havingValue = "no")
public final class NoMetricsRecorder implements MetricsRecorder {
    @Override
    public void record(String metricName, Timestamp timestamp, int value) {
        // empty / no action
    }
}
