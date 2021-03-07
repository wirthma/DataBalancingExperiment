package cz.cuni.mff.dbe.simulator;

import cz.cuni.mff.dbe.util.metrics.Metrics;
import cz.cuni.mff.dbe.util.metrics.MetricsRecorder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@Configuration
@ComponentScan("cz.cuni.mff.dbe")
@PropertySource("classpath:${algorithm}.properties")
@PropertySource("classpath:${workload}.properties")
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Main.class);

        Metrics.setMetricsRecorder(context.getBean(MetricsRecorder.class));

        context.getBean(Main.class).simulate();
    }

    private void simulate() {
        simulator.simulateInit();

        for (int i = 0; i < iterationCount; ++i) {
            simulator.simulateIteration();
        }
    }

    @Autowired
    private Simulator simulator;

    @Value("${iterationcount}")
    private int iterationCount;
}
