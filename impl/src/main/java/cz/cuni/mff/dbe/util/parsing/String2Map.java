package cz.cuni.mff.dbe.util.parsing;

import java.util.HashMap;
import java.util.Map;

/**
 * Parses a string representation of a map.
 */
public final class String2Map {
    /**
     * Parses a comma-separated list of colon-separated integer pairs to a map.
     *
     * An example input "42:100,21:200" gets parsed to a map of two pairs 42 -> 100 and 21 -> 200.
     */
    public static Map<Integer, Integer> parseIntPairMap(String mapStr) throws ParsingException {
        Map<Integer, Integer> map = new HashMap<>();

        for (String pairStr : mapStr.split(",")) {
            int colonIndex = pairStr.indexOf(':');
            if (colonIndex <= 0 || colonIndex == pairStr.length() - 1) {
                throw new ParsingException("Bad pair definition (probably missing colon): \"" + pairStr + "\"");
            }

            Integer key = parseInt(pairStr.substring(0, colonIndex));
            Integer value = parseInt(pairStr.substring(colonIndex + 1));

            if (!map.containsKey(key)) {
                map.put(key, value);
            } else {
                throw new ParsingException("Duplicated key in the parsed map string!");
            }
        }

        return map;
    }

    /**
     * Parses the given string to an integer while encapsulating any possible exceptions into an
     * {@link ParsingException}.
     */
    private static Integer parseInt(String intStr) throws ParsingException {
        try {
            return Integer.valueOf(intStr);
        } catch (Exception e) {
            throw new ParsingException("String does not represent an integer: \"" + intStr + "\"", e);
        }
    }
}
