package io.mobimenu.common.validation;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * This class provides a common bean validation interface for validating objects
 * according to the jakarta validation specification
 *
 * @param <T>   the type to validate
 */
public abstract class SelfValidating<T> {

    private final Validator validator;

    public SelfValidating() {
        validator = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory()
                .getValidator();
    }

    /**
     * Validates the object according to the annotations defined in the jakarta validation spec.
     * @throws ConstraintViolationException if there are validation errors
     */
    protected void validate() {
        var violations = validator.validate((T) this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
