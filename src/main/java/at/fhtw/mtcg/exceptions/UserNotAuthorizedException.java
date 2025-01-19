package at.fhtw.mtcg.exceptions;

public class UserNotAuthorizedException extends RuntimeException {
    public UserNotAuthorizedException(String message) {
        super(message);
    }

    public UserNotAuthorizedException() {
    }
}
