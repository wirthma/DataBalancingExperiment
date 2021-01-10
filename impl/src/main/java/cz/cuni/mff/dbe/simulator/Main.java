package cz.cuni.mff.dbe.simulator;

import cz.cuni.mff.dbe.algorithm.DataBalancingAlgorithm;
import cz.cuni.mff.dbe.algorithm.DummyDataBalancingAlgorithm;
import cz.cuni.mff.dbe.algorithm.RandomDataBalancingAlgorithm;
import cz.cuni.mff.dbe.databal.DataBalancer;
import cz.cuni.mff.dbe.datasimulator.DataSimulator;
import cz.cuni.mff.dbe.datasimulator.RandomIncrementalDataSimulator;
import cz.cuni.mff.dbe.loadsimulator.LoadSimulator;
import cz.cuni.mff.dbe.loadsimulator.NoLoadSimulator;
import cz.cuni.mff.dbe.nodecountsimulator.NodeCountSimulator;
import cz.cuni.mff.dbe.nodecountsimulator.StableNodeCountSimulator;
import cz.cuni.mff.dbe.util.metrics.ConsoleMetricsRecorder;
import cz.cuni.mff.dbe.util.metrics.CsvMetricsRecorder;
import cz.cuni.mff.dbe.util.metrics.Metrics;
import cz.cuni.mff.dbe.util.metrics.NoMetricsRecorder;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        // collection of params
        int initNodeCount = 3;
        int iterationCount = 5;
        DataBalancingAlgorithm dataBalancingAlgorithm = new RandomDataBalancingAlgorithm(1, 42);
        DataSimulator dataSimulator = new RandomIncrementalDataSimulator(42);
        LoadSimulator loadSimulator = new NoLoadSimulator();
        NodeCountSimulator nodeCountSimulator = new StableNodeCountSimulator(initNodeCount);

        Metrics.setMetricsRecorder(
                //new NoMetricsRecorder()
                new ConsoleMetricsRecorder()
                //new CsvMetricsRecorder(System.getProperty("user.dir") + File.separator + "metrics")
        );

        Simulator simulator = new Simulator(
                new DataBalancer(dataBalancingAlgorithm),
                dataSimulator,
                loadSimulator,
                nodeCountSimulator
        );

        simulator.simulateInit();

        for (int i = 0; i < iterationCount; ++i) {
            simulator.simulateIteration();
        }
    }
}
