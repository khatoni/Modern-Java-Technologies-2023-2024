package bg.sofia.uni.fmi.mjt.cooking.exceptions;

public class ClientErrorMessage extends Exception {
    public ClientErrorMessage() {
        super();
    }

    public ClientErrorMessage(String message) {
        super(message);
    }

    public ClientErrorMessage(String message, Throwable cause) {
        super(message, cause);
    }
}
