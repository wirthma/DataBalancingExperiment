package cz.cuni.mff.dbe.util.parsing;

/**
 * Represents an error during parsing.
 */
public class ParsingException extends Exception {
    public ParsingException() {
        super();
    }

    public ParsingException(String message) {
        super(message);
    }

    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }
}
