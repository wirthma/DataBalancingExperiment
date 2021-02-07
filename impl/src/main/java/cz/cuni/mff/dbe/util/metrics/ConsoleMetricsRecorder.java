package cz.cuni.mff.dbe.util.metrics;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * {@link MetricsRecorder} that writes metrics record on the standard output.
 */
@Component
@ConditionalOnProperty(name = "metricsrecorder", havingValue = "console")
public final class ConsoleMetricsRecorder implements MetricsRecorder {
    @Override
    public void record(String metricName, Timestamp timestamp, int value) {
        System.out.println(metricName + " >>> " + timestamp + " : " + value);
    }
}
