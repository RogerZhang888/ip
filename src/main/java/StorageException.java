/** Indicates that Alpha could not read or write its task storage file. */
public class StorageException extends Exception {
    private static final long serialVersionUID = 1L;

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
