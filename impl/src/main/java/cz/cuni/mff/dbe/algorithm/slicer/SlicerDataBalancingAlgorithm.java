package cz.cuni.mff.dbe.algorithm.slicer;

import cz.cuni.mff.dbe.algorithm.DataBalancingAlgorithm;
import cz.cuni.mff.dbe.model.DataDistributionChange;
import cz.cuni.mff.dbe.model.DataItem;
import cz.cuni.mff.dbe.model.Model;
import cz.cuni.mff.dbe.model.Node;
import cz.cuni.mff.dbe.util.data.DataDistributionUtils;
import cz.cuni.mff.dbe.util.ds.TokenRing;
import cz.cuni.mff.dbe.util.rand.KeyGen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A {@link DataBalancingAlgorithm} implementing the Google Slicer's Weighted-Move algorithm with no replication.
 */
@Component
@ConditionalOnProperty(name = "databalancingalgorithm", havingValue = "slicer")
public final class SlicerDataBalancingAlgorithm implements DataBalancingAlgorithm {
    /**
     * @param seed The seed for the pseudo-random generator.
     */
    public SlicerDataBalancingAlgorithm(
        @Value("${databalancingalgorithm.slicer.seed}") int seed
    ) {
        random = new Random(seed);
        dataItemKeyGen = new KeyGen<>(random);
    }

    @Override
    public DataDistributionChange runInit(Model model) {
        // equally split the key ring to slices and assigns them uniformly to all the nodes
        int nodeCount = model.getNodeSet().size();
        int sliceLength = Integer.MAX_VALUE / nodeCount;
        for (int i = 0; i < nodeCount; ++i) {
            slices.add(new Slice(sliceLength * i), model.getNodeSet().getNth(i).getValue());
        }

        return assignDataItems(model);
    }

    @Override
    public DataDistributionChange runIteration(int iterationNumber, Model model) {
        reassignCrashedNodes(model);

        mergeCoolAdjacentSlices(model);

        // TODO

        return assignDataItems(model);
    }

    /**
     * Performs the moving phase of the Weighted-Move algorithm.
     */
    private void moveSlices(Model model) {
        int movedDataItemsThreshold = computeDataItemCount(model) / 100 * 9; // 9%
        int movedDataItems = 0;

        while (movedDataItems < movedDataItemsThreshold) {
            Node leastLoadedNode = computeLeastLoadedNode(model).getValue();
            Node mostLoadedNode = computeMostLoadedNode(model).getValue();

            List<Slice> mostLoadedNodeSlices = computeNodeSliceMap(model).get(mostLoadedNode);
        }
    }

    /**
     * Performs the merging phase of the Weighted-Move algorithm.
     */
    private void mergeCoolAdjacentSlices(Model model) {
        int movedDataItemCount = 0;
        int movedDataItemsThreshold = computeDataItemCount(model) / 100; // 1%
        boolean hasSlicesBeenMerged = true; // this initial value is technical and does not make sense
        Slice sliceA = slices.getLeast().getKey();
        Slice sliceB = slices.getStrictlyNext(sliceA).getKey();

        // runs over the ring, keeping sliceA and sliceB as always two following slices on the ring
        while (true) {
            // breaks if the last ring run did not merge anything
            if (sliceA.equals(slices.getLeast().getKey())) {
                if (!hasSlicesBeenMerged) {
                    break;
                }
                hasSlicesBeenMerged = false;
            }

            // breaks on the simple conditions of Weighted-Move
            if (computeSlicesPerNodeAvg((model)) < 50 || movedDataItemCount > movedDataItemsThreshold) {
                break;
            }

            // merges on conditions of Weighted-Move and shifts one slice further either way
            Map<Slice, Integer> sliceLoads = computeSliceLoadMap(model);
            Map<Node, Integer> nodeLoads = computeNodeLoadMap(model);
            Node nodeA = slices.get(sliceA);
            Node nodeB = slices.get(sliceB);
            Node lessLoadedNode = nodeLoads.get(nodeA) <= nodeLoads.get(nodeB) ? nodeA : nodeB;
            int futureLoadOfLessLoadedNode = nodeLoads.get(lessLoadedNode)
                                             + (lessLoadedNode.equals(nodeA) ? sliceLoads.get(sliceA)
                                                                             : sliceLoads.get(sliceB));
            int mergedSliceLoad = sliceLoads.get(sliceA) + sliceLoads.get(sliceB);
            if (
                mergedSliceLoad < computeSliceLoadAvg(model)
                && futureLoadOfLessLoadedNode < computeMostLoadedNode(model).getKey()
            ) {
                hasSlicesBeenMerged = true;

                // merge - simple as this, everything will recompute automatically as we compute everything dynamically
                slices.remove(sliceA);
                slices.remove(sliceB);
                slices.add(sliceA, lessLoadedNode);

                // shifts slice B; sliceA stays the same, it automatically represents the merged slice now
                sliceB = slices.getStrictlyNext(sliceA).getKey();
            } else {
                // just shifts
                sliceA = sliceB;
                sliceB = slices.getStrictlyNext(sliceB).getKey();
            }
        }
    }

    /**
     * Returns the node with the max load in the system.
     */
    private Map.Entry<Integer, Node> computeMostLoadedNode(Model model) {
        return computeLoadNodeMap(model).lastEntry();
    }

    /**
     * Returns the node with the min load in the system.
     */
    private Map.Entry<Integer, Node> computeLeastLoadedNode(Model model) {
        return computeLoadNodeMap(model).firstEntry();
    }

    /**
     * Return the average number of slices per one system node.
     */
    private int computeSlicesPerNodeAvg(Model model) {
        return slices.size() / model.getNodeSet().size();
    }

    /**
     * Returns the average load per one slice.
     */
    private int computeSliceLoadAvg(Model model) {
        return computeLoadSum(model) / slices.size();
    }

    /**
     * Returns the average load per one system node.
     */
    private int computeNodeLoadAvg(Model model) {
        return computeLoadSum(model) / model.getNodeSet().size();
    }

    /**
     * Returns the number of item in the system.
     */
    private int computeDataItemCount(Model model) {
        return (int)model.getDataDistribution().getNodeToDataMap().values().stream().mapToLong(
            Collection::size
        ).sum();
    }

    /**
     * Return a map of system nodes to total load of data items placed on that nodes.
     */
    private Map<Node, Integer> computeNodeLoadMap(Model model) {
        Map<Node, Integer> nodeLoadMap = new HashMap<>();

        model.getLoadDistribution().getItemToLoadMap().forEach(
            (DataItem item, Integer load) -> nodeLoadMap.merge(getOwningNode(item), load, Integer::sum)
        );

        return nodeLoadMap;
    }

    /**
     * Return reversed map to {@link #computeNodeLoadMap(Model)}
     */
    private TreeMap<Integer, Node> computeLoadNodeMap(Model model) {
        TreeMap<Integer, Node> loadNodeMap = new TreeMap<>();

        computeNodeLoadMap(model).forEach((Node node, Integer load) -> loadNodeMap.put(load, node));

        return loadNodeMap;
    }

    /**
     * Return a map of slices to total load of data items placed on that nodes.
     */
    private Map<Slice, Integer> computeSliceLoadMap(Model model) {
        Map<Slice, Integer> sliceLoadMap = new HashMap<>();

        model.getLoadDistribution().getItemToLoadMap().forEach(
            (DataItem item, Integer load) -> sliceLoadMap.merge(getOwningSlice(item), load, Integer::sum)
        );

        return sliceLoadMap;
    }

    /**
     * Return a map of nodes to slices placed on that nodes.
     */
    private Map<Node, List<Slice>> computeNodeSliceMap(Model model) {
        Map<Node, List<Slice>> nodeSliceMap = new HashMap<>();

        slices.getAll().forEach((Map.Entry<Slice, Node> e) -> {
            List<Slice> nodeSlices = nodeSliceMap.get(e.getValue());
            if (nodeSlices == null) {
                nodeSlices = new ArrayList<>();
            }

            nodeSlices.add(e.getKey());

            nodeSliceMap.put(e.getValue(), nodeSlices);
        });

        return nodeSliceMap;
    }

    /**
     * Returns a map of slices to the number of data items placed on that slices.
     */
    private Map<Slice, Integer> computeSliceDataItemCounts(Model model) {
        Map<Slice, Integer> sliceDataItemCounts = new HashMap<>();

        model.getDataDistribution().getNodeToDataMap().values().stream().flatMap(
            Collection::stream
        ).forEach(
            (DataItem item) -> sliceDataItemCounts.merge(getOwningSlice(item), 1, Integer::sum)
        );

        return sliceDataItemCounts;
    }

    /**
     * Returns the total load on the system.
     */
    private int computeLoadSum(Model model) {
        return model.getLoadDistribution().getItemToLoadMap().values().stream().reduce(0, Integer::sum);
    }

    /**
     * Assigns the data items to nodes based on their membership to slices, which are mapped to those nodes in {@link
     * #slices}.
     */
    private DataDistributionChange assignDataItems(Model model) {
        Map<Node, List<DataItem>> createdItems = new HashMap<>();
        Map<Node, List<DataItem>> removedItems = new HashMap<>();

        for (Map.Entry<Node, List<DataItem>> nodeItems : model.getDataDistribution().getNodeToDataMap().entrySet()) {
            Node oldNode = nodeItems.getKey();
            for (DataItem item : nodeItems.getValue()) {
                Node newNode = getOwningNode(item);

                if (!newNode.equals(oldNode)) {
                    DataDistributionUtils.addToMap(oldNode, item, removedItems);
                    DataDistributionUtils.addToMap(newNode, item, createdItems);
                }
            }
        }
        return new DataDistributionChange(createdItems, removedItems);
    }

    /**
     * Reassigns slices mapped in {@link #slices} to nodes that no longer exist in the given {@link Model}
     * to a random existing node.
     */
    private void reassignCrashedNodes(Model model) {
        List<Map.Entry<Slice, Node>> slicesOfCrashedNodes = slices.getAll().stream().filter(
            (Map.Entry<Slice, Node> entry) -> model.getNodeSet().get(entry.getValue().getId()) != null
        ).collect(Collectors.toList());

        slicesOfCrashedNodes.forEach(
            (Map.Entry<Slice, Node> entry) -> {
                Node randomNode = model.getNodeSet().getNth(random.nextInt(model.getNodeSet().size())).getValue();

                slices.remove(entry.getKey());
                slices.add(entry.getKey(), randomNode);
            }
        );
    }

    /**
     * Returns a random key for the given data item (stably generated), representing a hash of that data item.
     */
    private Integer getDataItemKey(DataItem dataItem) {
        return dataItemKeyGen.getKey(dataItem);
    }

    /**
     * Returns the {@link Slice} where the given {@link DataItem} should belongs to according to {@link #slices}.
     */
    private Slice getOwningSlice(DataItem dataItem) {
        return slices.getPreviousOnRing(new Slice(getDataItemKey(dataItem))).getKey();
    }

    /**
     * Returns the {@link Node} where the given {@link DataItem} should belongs to according to {@link #slices}.
     */
    private Node getOwningNode(DataItem dataItem) {
        return slices.getPreviousOnRing(new Slice(getDataItemKey(dataItem))).getValue();
    }

    /**
     * Stores mapping of slices onto system nodes.
     * <p>
     * The interface of {@link TokenRing} is appropriate for queries for membership of a data item's key to the owning
     * slice, and in this setup, also to the owning node.
     */
    private final TokenRing<Slice, Node> slices = new TokenRing<>();

    private final Random random;

    /**
     * Provides random keys for data items. Each data item has exactly one unique key, permanently assigned.
     */
    private final KeyGen<DataItem> dataItemKeyGen;
}
