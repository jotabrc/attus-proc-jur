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
public class ValidatorProcessNumber implements ConstraintValidator<ValidProcessNumber, String> {

    @Value("${validation.pattern.processnumber}")
    private String regex;

    @Override
    public void initialize(ValidProcessNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (ParameterCheck.isNullOrBlank(s)) return false;
        Pattern pattern = Pattern.compile(Optional.ofNullable(regex)
                .orElse("\\p{a-f0-9}{8}-\\p{a-f0-9}{4}-\\p{a-f0-9}{4}-\\p{a-f0-9}{12}"));
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
