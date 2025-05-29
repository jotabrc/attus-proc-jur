package attus.proc.proc_jur.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidatorName.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
public @interface ValidName {
    String message() default "Invalid NAME format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
