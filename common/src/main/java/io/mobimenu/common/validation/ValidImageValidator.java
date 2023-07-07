package io.mobimenu.common.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Base64;
import java.util.regex.Pattern;

@Slf4j
public class ValidImageValidator implements ConstraintValidator<ValidImage, String> {
    private static final Pattern BASE64_IMAGE_PATTERN = Pattern.compile("data:([-\\w]+\\/[-+\\w.]+)?(;?\\w+=[-\\w]+)*(;base64)?,.*");
    private static final String REGEX = "base64,";

    private String message;
    private int permittedSizeInBytes;


    @Override
    public void initialize(ValidImage validImage) {
        message = validImage.message();
        permittedSizeInBytes = validImage.maxSize();
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        try {
            String validationMessage = validateImageContent(value);
            if (validationMessage != null && !validationMessage.isBlank()) {
                context.buildConstraintViolationWithTemplate(!message.isBlank() ? message : validationMessage)
                        .addConstraintViolation();
                return false;
            }
        } catch (IllegalArgumentException | StringIndexOutOfBoundsException exception) {
            log.warn("Could not validate file... ", exception);
            context.buildConstraintViolationWithTemplate("is invalid")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }

    private String validateImageContent(String value) {
        if(!BASE64_IMAGE_PATTERN.matcher(value).matches()) {
            return "contains invalid content type";
        }

        var decodedImage = Base64.getDecoder().decode(value.split(REGEX)[1]);
        if (decodedImage.length > permittedSizeInBytes) {
            return "is of invalid size";
        }

        return null;
    }
}
