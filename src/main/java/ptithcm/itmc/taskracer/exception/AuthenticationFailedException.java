package ptithcm.itmc.taskracer.exception;

import lombok.Getter;

@Getter
public class AuthenticationFailedException extends RuntimeException{
    private final String message;
    public AuthenticationFailedException(String message) {
        super(message);
        this.message = message;
    }
}
