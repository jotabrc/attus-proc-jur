package attus.proc.proc_jur.validation;

import jakarta.validation.ConstraintValidatorContext;

public class ConstraintMessage {

    private ConstraintMessage() {}

    public static void createConstraintMessage(final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(context
                        .getDefaultConstraintMessageTemplate())
                .addConstraintViolation();
    }
}