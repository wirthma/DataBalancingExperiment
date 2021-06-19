package cz.cuni.mff.dbe.util.metrics;

import cz.cuni.mff.dbe.util.log.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;

/**
 * {@link MetricsWriter} that writes metrics record in a CSV formatted file named as the metric.
 */
@Component
@ConditionalOnProperty(name = "metricswriter", havingValue = "csv")
public final class CsvMetricsWriter implements MetricsWriter {
    /**
     * @param workingDir Path to a directory where the metrics are being stored.
     */
    public CsvMetricsWriter(
            @Value("${metricswriter.csv.workingDir}") String workingDir
    ) {
        this.workingDir = workingDir;
    }

    /**
     * Appends the given metric record to the CSV file.
     */
    @Override
    public void record(String metricName, Timestamp timestamp, int value) {
        String metricFile = workingDir + File.separator + metricName;

        try {
            Files.createDirectories(Paths.get(workingDir));
        } catch (IOException e) {
            Log.getLogger().log(Level.SEVERE, "Cannot create dir {0}", workingDir);
        }

        try (
                BufferedWriter writer = Files.newBufferedWriter(
                        Paths.get(metricFile),
                        StandardOpenOption.CREATE,
                        StandardOpenOption.APPEND
                )
        ) {
            writer.write(timestamp + "," + value + System.lineSeparator());
        } catch (IOException e) {
            Log.getLogger().log(
                    Level.SEVERE,
                    "Cannot write to a metric file {0}: {1}",
                    new Object[] {metricFile, e}
            );
        }
    }

    /**
     * Path to a directory where the metrics are being stored.
     */
    private String workingDir = null;
}
