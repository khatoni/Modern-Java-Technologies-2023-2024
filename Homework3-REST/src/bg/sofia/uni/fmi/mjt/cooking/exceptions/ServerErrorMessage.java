package bg.sofia.uni.fmi.mjt.cooking.exceptions;

public class ServerErrorMessage extends Exception {

    public ServerErrorMessage() {
        super();
    }

    public ServerErrorMessage(String message) {
        super(message);
    }

    public ServerErrorMessage(String message, Throwable cause) {
        super(message, cause);
    }
}
