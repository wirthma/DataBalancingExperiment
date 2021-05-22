package cz.cuni.mff.dbe.algorithm;

import cz.cuni.mff.dbe.model.*;
import cz.cuni.mff.dbe.util.data.DataDistributionUtils;
import cz.cuni.mff.dbe.util.rand.KeyGen;
import cz.cuni.mff.dbe.util.rand.KeySetGen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * A {@link DataBalancingAlgorithm} providing a standard consistent hashing implementation.
 */
@Component
@ConditionalOnProperty(name = "databalancingalgorithm", havingValue = "consistenthashing")
public final class ConsistentHashingDataBalancingAlgorithm implements DataBalancingAlgorithm {
    /**
     * @param seed The seed for the pseudo-random generator.
     */
    public ConsistentHashingDataBalancingAlgorithm(
        @Value("${databalancingalgorithm.consistenthashing.tokensPerNode}") int tokensPerNode,
        @Value("${databalancingalgorithm.consistenthashing.seed}") int seed
    ) {
        random = new Random(seed);
        dataItemKeyGen = new KeyGen<>(random);
        tokensGen = new KeySetGen<>(tokensPerNode, random);
    }

    @Override
    public DataDistributionChange runInit(Model model) {
        return rebalance(model);
    }

    @Override
    public DataDistributionChange runIteration(int iterationNumber, Model model) {
        return rebalance(model);
    }

    private DataDistributionChange rebalance(Model model) {
        Map<Node, List<DataItem>> createdItems = new HashMap<>();
        Map<Node, List<DataItem>> removedItems = new HashMap<>();

        NodeSet tokenRing = constructTokenRing(model); // we repeat this each iteration to get an up-to-date token ring

        for (Map.Entry<Node, List<DataItem>> nodeItems : model.getDataDistribution().getNodeToDataMap().entrySet()) {
            Node originalNode = nodeItems.getKey();
            List<DataItem> items = nodeItems.getValue();

            for (DataItem item : items) {
                // mapping: item -> key -> next token on ring -> token's corresponding node
                int itemKey = dataItemKeyGen.getKey(item);
                Node token = tokenRing.getNextOnRing(itemKey);
                Node belongsToNode = tokensGen.getObject(token.getId());

                if (!originalNode.equals(belongsToNode)) {
                    DataDistributionUtils.addToMap(belongsToNode, item, createdItems);
                    DataDistributionUtils.addToMap(originalNode, item, removedItems);
                }
            }
        }

        return new DataDistributionChange(createdItems, removedItems);
    }

    /**
     * Constructs a convenient data structure storing tokens of all system nodes and providing ring-interval queries.
     */
    private NodeSet constructTokenRing(Model model) {
        NodeSet tokenRing = new NodeSet();

        model.getNodes().getAll().stream().flatMap(
            (Node node) -> tokensGen.getKeySet(node).stream()
        ).forEach(
            (Integer token) -> tokenRing.add(new Node(token))
        );

        return tokenRing;
    }

    /**
     * Provides random keys for data items. Each data item has exactly one unique key, permanently assigned.
     */
    private KeyGen<DataItem> dataItemKeyGen;

    /**
     * Provides random key-sets for system nodes. Each node has exactly an in-constructor-specified number of
     * unique keys, permanently assigned. These keys are sometimes called tokens or virtual nodes.
     */
    private KeySetGen<Node> tokensGen;

    private Random random;
}
