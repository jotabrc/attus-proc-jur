package attus.proc.proc_jur.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidatorLegalId.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLegalId {
    String message() default "Invalid Legal Entity Identifier format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
