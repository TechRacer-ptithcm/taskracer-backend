package ptithcm.itmc.taskracer.exception;

import lombok.Getter;

@Getter
public class ValidationFailedException extends RuntimeException{
    private final String message;
    public ValidationFailedException(String message) {
        super(message);
        this.message = message;
    }
}
