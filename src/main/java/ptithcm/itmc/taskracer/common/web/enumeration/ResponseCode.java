package ptithcm.itmc.taskracer.common.web.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    SUCCESS("000000", "Operation succeeded."),
    CREATE_SUCCESS("000001", "Creation succeeded."),
    ERROR("999999", "Something is wrong with our system. Please try again later."),
    USER_NOT_FOUND("100000", "User not found."),
    USER_NOT_ACTIVE("100001", "User is not active."),
    USER_BLOCKED("100002", "User is blocked."),
    RESOURCE_NOT_FOUND("200001", "Resource not found."),
    TIER_INSUFFICIENT("300001", "Tier is insufficient."),
    ROLE_INSUFFICIENT("300002", "Role is insufficient."),
    AUTHENTICATION_FAILED("400001", "Authentication failed."),
    VALIDATE_FAILED("500001", "Validate failed."),
    DUPLICATE_DATA("500002", "Duplicate data."),
    MISSING_FIELD("600001", "Missing field.");


    private final String code;
    private final String message;
}
