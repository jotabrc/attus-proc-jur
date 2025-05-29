package attus.proc.proc_jur.validation;

import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.util.ParameterCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ValidatorStatus implements ConstraintValidator<ValidStatus, Status> {

    @Override
    public void initialize(ValidStatus constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Status s, ConstraintValidatorContext context) {
        if (ParameterCheck.isNull(s)) return false;
        return Arrays.asList(Status.values()).contains(s);
    }
}
