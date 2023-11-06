package bg.sofia.uni.fmi.mjt.gym.member;

public class DayOffException extends RuntimeException {
    DayOffException() {

        super();
    }

    DayOffException(String message) {

        super(message);
    }

    DayOffException(String message, Throwable cause) {

        super(message, cause);
    }
}
