package ptithcm.itmc.taskracer.exception;

import lombok.Getter;

@Getter
public class RoleInsufficientException extends RuntimeException{
    private final String message;
    public RoleInsufficientException(String message) {
        super(message);
        this.message = message;
    }
}
