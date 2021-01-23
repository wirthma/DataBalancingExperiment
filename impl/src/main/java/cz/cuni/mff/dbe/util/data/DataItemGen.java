package cz.cuni.mff.dbe.util.data;

import cz.cuni.mff.dbe.model.DataItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator of {@link DataItem data items} and their collections.

 * This is a singleton object.
 */
public final class DataItemGen {
    public static DataItem generateOne() {
        return new DataItem(nextDataItemId++);
    }

    public static List<DataItem> generateList(int number) {
        List<DataItem> items = new ArrayList<>();
        for (int i = 0; i < number; ++i) {
            items.add(generateOne());
        }
        return items;
    }

    private static int nextDataItemId = 0;
}
