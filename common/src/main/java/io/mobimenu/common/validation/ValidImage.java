package io.mobimenu.common.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ValidImageValidator.class)
public @interface ValidImage {
    String message() default "";

    /* Maximum size in bytes  */
    int maxSize() default 500000;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
