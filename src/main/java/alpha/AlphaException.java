package alpha;

/** Represents an error caused by an invalid Alpha command. */
public class AlphaException extends Exception {
    private static final long serialVersionUID = 1L;

    public AlphaException(String message) {
        super(message);
    }
}
