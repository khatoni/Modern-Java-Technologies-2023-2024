package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseNotFoundException extends java.lang.Exception{
    public CourseNotFoundException(){
        super();
    }
    public CourseNotFoundException(String message) {
        super(message);
    }
    public CourseNotFoundException(String message,Throwable cause) {
        super(message, cause);
    }
}
