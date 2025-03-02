package ptithcm.itmc.taskracer.exception;

import lombok.Getter;

@Getter
public class ResourceNotFound extends RuntimeException{
    private final String message;
    public ResourceNotFound(String message) {
        super(message);
        this.message = message;
    }
}
