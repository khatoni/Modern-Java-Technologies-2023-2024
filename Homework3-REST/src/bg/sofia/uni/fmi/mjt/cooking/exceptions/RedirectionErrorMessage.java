package bg.sofia.uni.fmi.mjt.cooking.exceptions;

public class RedirectionErrorMessage extends Exception {
    public RedirectionErrorMessage() {
        super();
    }

    public RedirectionErrorMessage(String message) {
        super(message);
    }

    public RedirectionErrorMessage(String message, Throwable cause) {
        super(message, cause);
    }
}
