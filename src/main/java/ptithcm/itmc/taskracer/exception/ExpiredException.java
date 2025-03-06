package ptithcm.itmc.taskracer.exception;

import lombok.Getter;

@Getter
public class ExpiredException extends RuntimeException {
    private final String message;

    public ExpiredException(String message) {
        super(message);
        this.message = message;
    }
}
