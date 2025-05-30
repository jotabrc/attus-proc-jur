package attus.proc.proc_jur.validation;

import attus.proc.proc_jur.util.ParameterCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidatorName implements ConstraintValidator<ValidName, String> {

    @Value("${validation.pattern.name}")
    private String regex;

    @Override
    public void initialize(ValidName constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (ParameterCheck.isNullOrBlank(s)) {
            ConstraintMessage.createConstraintMessage(context);
            return false;
        }
        Pattern pattern = Pattern.compile(Optional.ofNullable(regex)
                .orElse("[\\p{L}\\p{M}\\.\\s'-]{4,255}"));
        Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) {
            ConstraintMessage.createConstraintMessage(context);
            return false;
        }
        return true;
    }
}
