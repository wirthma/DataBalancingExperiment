package cz.cuni.mff.dbe.util.log;

import java.util.logging.Logger;

/**
 * Helper for logging.
 */
public final class Log {
    public static Logger getLogger() {
        return logger;
    }

    private static Logger logger = Logger.getLogger("cz.cuni.mff.dbe");
}
