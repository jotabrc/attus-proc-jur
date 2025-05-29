package attus.proc.proc_jur.validation;

import attus.proc.proc_jur.util.CheckString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidatorLegalId implements ConstraintValidator<ValidLegalId, String> {

    @Value("${validation.pattern.legalId}")
    private String regex;

    @Override
    public void initialize(ValidLegalId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (CheckString.isNullOrBlank(s)) return false;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
