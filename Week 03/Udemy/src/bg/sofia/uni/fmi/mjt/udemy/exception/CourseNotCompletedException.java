package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseNotCompletedException extends java.lang.Exception{
    public CourseNotCompletedException(){
        super();
    }
    public CourseNotCompletedException(String message) {
        super(message);
    }
    public CourseNotCompletedException(String message,Throwable cause) {
        super(message, cause);
    }
}
