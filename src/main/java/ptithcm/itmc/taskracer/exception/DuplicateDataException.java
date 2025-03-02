package ptithcm.itmc.taskracer.exception;

import lombok.Getter;

@Getter
public class DuplicateDataException extends RuntimeException{
    private final String message;
    public DuplicateDataException(String message) {
        super(message);
        this.message = message;
    }
}
