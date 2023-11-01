package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseAlreadyPurchasedException extends java.lang.Exception{
    public CourseAlreadyPurchasedException(){
        super();
    }
    public CourseAlreadyPurchasedException(String message) {
        super(message);
    }
    public CourseAlreadyPurchasedException(String message,Throwable cause) {
        super(message, cause);
    }
}
