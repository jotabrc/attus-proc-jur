package attus.proc.proc_jur.validation;

import attus.proc.proc_jur.enums.ActionType;
import attus.proc.proc_jur.enums.Status;
import attus.proc.proc_jur.util.ParameterCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ValidatorActionType implements ConstraintValidator<ValidActionType, ActionType> {

    @Override
    public void initialize(ValidActionType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ActionType a, ConstraintValidatorContext context) {
        if (ParameterCheck.isNull(a)) return false;
        return Arrays.asList(ActionType.values()).contains(a);
    }
}
