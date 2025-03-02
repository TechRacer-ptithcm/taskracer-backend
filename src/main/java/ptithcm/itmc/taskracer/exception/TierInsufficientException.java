package ptithcm.itmc.taskracer.exception;

import lombok.Getter;

@Getter
public class TierInsufficientException extends RuntimeException{
    private final String message;
    public TierInsufficientException(String message) {
        super(message);
        this.message = message;
    }
}
