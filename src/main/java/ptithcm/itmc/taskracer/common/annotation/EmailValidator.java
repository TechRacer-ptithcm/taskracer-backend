package ptithcm.itmc.taskracer.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidateEmail, String> {
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && email.matches(EMAIL_REGEX);
    }
}
