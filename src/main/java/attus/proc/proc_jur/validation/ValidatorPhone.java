package attus.proc.proc_jur.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ValidatorPhone implements ConstraintValidator<ValidPhone, String> {

    @Value("${validation.pattern.phone}")
    private String regex;

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile(Optional.ofNullable(regex)
                .orElse("^\\d{10,15}$|^\\+?\\d{1,3}[\\s-]?\\(?\\d{2,4}\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$"));
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }
}
