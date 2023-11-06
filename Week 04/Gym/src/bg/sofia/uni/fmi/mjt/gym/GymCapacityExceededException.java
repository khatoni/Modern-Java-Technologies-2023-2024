package bg.sofia.uni.fmi.mjt.gym;

public class GymCapacityExceededException extends Exception {
    GymCapacityExceededException() {
        super();
    }

    GymCapacityExceededException(String message) {
        super(message);
    }

    GymCapacityExceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
