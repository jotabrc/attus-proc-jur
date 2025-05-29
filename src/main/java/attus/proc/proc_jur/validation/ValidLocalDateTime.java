package attus.proc.proc_jur.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidatorLocalDateTime.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface ValidLocalDateTime {
    String message() default "Invalid Local Date Time format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
