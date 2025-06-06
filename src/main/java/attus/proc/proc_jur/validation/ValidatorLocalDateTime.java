package attus.proc.proc_jur.validation;

import attus.proc.proc_jur.util.ParameterCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ValidatorLocalDateTime implements ConstraintValidator<ValidLocalDateTime, LocalDateTime> {

    @Override
    public void initialize(ValidLocalDateTime constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext context) {
        if (ParameterCheck.isNull(dateTime)) {
            ConstraintMessage.createConstraintMessage(context);
            return false;
        }
        return true;
    }
}
