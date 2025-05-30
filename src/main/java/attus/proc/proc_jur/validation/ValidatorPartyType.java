package attus.proc.proc_jur.validation;

import attus.proc.proc_jur.enums.PartyType;
import attus.proc.proc_jur.util.ParameterCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ValidatorPartyType implements ConstraintValidator<ValidPartyType, PartyType> {

    @Override
    public void initialize(ValidPartyType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PartyType p, ConstraintValidatorContext context) {
        if (ParameterCheck.isNull(p) || !Arrays.asList(PartyType.values()).contains(p)) {
            ConstraintMessage.createConstraintMessage(context);
            return false;
        }
        return true;
    }
}
