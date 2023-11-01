package bg.sofia.uni.fmi.mjt.udemy.exception;

public class InsufficientBalanceException extends java.lang.Exception{
    public InsufficientBalanceException() {
        super();
    }
    public InsufficientBalanceException(String message) {
        super(message);
    }
    public InsufficientBalanceException(String message,Throwable cause) {
        super(message, cause);
    }
}
