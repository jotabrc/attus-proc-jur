package attus.proc.proc_jur.validation;

import attus.proc.proc_jur.util.ParameterCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

@Component
public class ValidatorEmail implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (ParameterCheck.isNullOrBlank(s) || !EmailValidator.getInstance().isValid(s)) {
            ConstraintMessage.createConstraintMessage(context);
            return false;
        }
        return true;
    }
}
