package ptithcm.itmc.taskracer.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ptithcm.itmc.taskracer.common.web.enumeration.ResponseCode;
import ptithcm.itmc.taskracer.common.web.response.ErrorObject;
import ptithcm.itmc.taskracer.common.web.response.ResponseAPI;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ResponseAPI<?>> handlingAuthenticationFailedException(AuthenticationFailedException exception) {
        var response = ResponseAPI.<ErrorObject>builder()
                .code(ResponseCode.AUTHENTICATION_FAILED.getCode())
                .message(ResponseCode.AUTHENTICATION_FAILED.getMessage())
                .status(false)
                .data(new ErrorObject(exception.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ResponseAPI<?>> handlingResourceNotFound(ResourceNotFound exception) {
        log.error("Resource Exception: {}", (Object) exception.getStackTrace());
        var response = ResponseAPI.<ErrorObject>builder()
                .code(ResponseCode.USER_NOT_FOUND.getCode())
                .message(ResponseCode.USER_NOT_FOUND.getMessage())
                .status(false)
                .data(new ErrorObject(exception.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ResponseAPI<?>> handlingDuplicateDataException(DuplicateDataException exception) {
        var response = ResponseAPI.<ErrorObject>builder()
                .code(ResponseCode.DUPLICATE_DATA.getCode())
                .message(ResponseCode.DUPLICATE_DATA.getMessage())
                .status(false)
                .data(new ErrorObject(exception.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MissingFieldException.class)
    public ResponseEntity<ResponseAPI<?>> handlingMissingFieldException(MissingFieldException exception) {
        var response = ResponseAPI.<ErrorObject>builder()
                .code(ResponseCode.MISSING_FIELD.getCode())
                .message(ResponseCode.MISSING_FIELD.getMessage())
                .status(false)
                .data(new ErrorObject(exception.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<ResponseAPI<?>> handlingValidationFailedException(ValidationFailedException exception) {
        var response = ResponseAPI.<ErrorObject>builder()
                .code(ResponseCode.VALIDATE_FAILED.getCode())
                .message(ResponseCode.VALIDATE_FAILED.getMessage())
                .status(false)
                .data(new ErrorObject(exception.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ExpiredException.class)
    public ResponseEntity<ResponseAPI<?>> handlingExpiredException(ExpiredException exception) {
        var response = ResponseAPI.<ErrorObject>builder()
                .code(ResponseCode.EXPIRED_CODE.getCode())
                .message(ResponseCode.EXPIRED_CODE.getMessage())
                .status(false)
                .data(new ErrorObject(exception.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(RoleInsufficientException.class)
    public ResponseEntity<ResponseAPI<?>> handlingRoleInsufficientException(RoleInsufficientException exception) {
        var response = ResponseAPI.<ErrorObject>builder()
                .code(ResponseCode.ROLE_INSUFFICIENT.getCode())
                .message(ResponseCode.ROLE_INSUFFICIENT.getMessage())
                .status(false)
                .data(new ErrorObject(exception.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(TierInsufficientException.class)
    public ResponseEntity<ResponseAPI<?>> handlingTierInsufficientException(TierInsufficientException exception) {
        var response = ResponseAPI.<ErrorObject>builder()
                .code(ResponseCode.TIER_INSUFFICIENT.getCode())
                .message(ResponseCode.TIER_INSUFFICIENT.getMessage())
                .status(false)
                .data(new ErrorObject(exception.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseAPI<?>> handlingException(Exception exception) {
        var response = ResponseAPI.<ErrorObject>builder()
                .code(ResponseCode.ERROR.getCode())
                .message(ResponseCode.ERROR.getMessage())
                .status(false)
                .data(new ErrorObject(exception.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
