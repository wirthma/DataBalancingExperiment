package cz.cuni.mff.dbe.datasimulator;

import cz.cuni.mff.dbe.model.*;
import cz.cuni.mff.dbe.util.data.DataItemGen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A {@link DataSimulator} that adds one data item at a time into random node in the system.
 */
@Component
@ConditionalOnProperty(name = "datasimulator", havingValue = "randomincremental")
public final class RandomIncrementalDataSimulator implements DataSimulator {
    /**
     * @param seed The seed for the pseudo-random generator.
     */
    public RandomIncrementalDataSimulator(
            @Value("${datasimulator.randomincremental.seed}") int seed
    ) {
        random = new Random(seed);
    }

    @Override
    public DataDistributionChange nextDataDistribution(
            int iterationNumber,
            DataDistribution dataDistribution,
            NodeSet nodes
    ) {
        if (nodes.isEmpty()) {
            return new DataDistributionChange();
        }

        Map<Node, List<DataItem>> createdDataItems = new HashMap<>();
        createdDataItems.put(nodes.getNth(random.nextInt(nodes.size())), DataItemGen.generateList(1));
        return new DataDistributionChange(createdDataItems, new HashMap<>());
    }

    private final Random random;
}
