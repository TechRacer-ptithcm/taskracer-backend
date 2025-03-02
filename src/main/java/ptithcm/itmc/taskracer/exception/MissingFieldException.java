package ptithcm.itmc.taskracer.exception;

import lombok.Getter;

@Getter
public class MissingFieldException extends RuntimeException{
    private final String message;
    public MissingFieldException(String message) {
        super(message);
        this.message = message;
    }
}
