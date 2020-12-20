package cz.cuni.mff.dbe.simulator;

import cz.cuni.mff.dbe.algorithm.DataBalancingAlgorithm;
import cz.cuni.mff.dbe.algorithm.DummyDataBalancingAlgorithm;
import cz.cuni.mff.dbe.databal.DataBalancer;
import cz.cuni.mff.dbe.datasimulator.DataSimulator;
import cz.cuni.mff.dbe.datasimulator.RandomIncrementalDataSimulator;
import cz.cuni.mff.dbe.loadsimulator.LoadSimulator;
import cz.cuni.mff.dbe.loadsimulator.NoLoadSimulator;
import cz.cuni.mff.dbe.nodecountsimulator.NodeCountSimulator;
import cz.cuni.mff.dbe.nodecountsimulator.StableNodeCountSimulator;

public class Main {
    public static void main(String[] args) {
        // collection of params
        int initNodeCount = 2;
        int iterationCount = 2;
        DataBalancingAlgorithm dataBalancingAlgorithm = new DummyDataBalancingAlgorithm();
        DataSimulator dataSimulator = new RandomIncrementalDataSimulator();
        LoadSimulator loadSimulator = new NoLoadSimulator();
        NodeCountSimulator nodeCountSimulator = new StableNodeCountSimulator(initNodeCount);

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
