/**
 * Represents an error caused by a command that Beemo cannot process.
 */
public class BeemoException extends Exception {
    public BeemoException(String message) {
        super(message);
    }
}
