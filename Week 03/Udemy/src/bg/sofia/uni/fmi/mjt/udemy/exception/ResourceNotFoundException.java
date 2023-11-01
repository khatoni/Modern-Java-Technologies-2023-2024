package bg.sofia.uni.fmi.mjt.udemy.exception;

public class ResourceNotFoundException extends java.lang.Exception {
    public ResourceNotFoundException(){
        super();
    }
    public ResourceNotFoundException(String message){
        super(message);
    }
    public ResourceNotFoundException(String message,Throwable cause) {
        super(message, cause);
    }
}
