package com.validator.trade.validators.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.validator.trade.validators.BeforeValidator;

/**
 * Annotation for {@link BeforeValidator}.
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {BeforeValidator.class})
public @interface Before {

    /**
     * Default message.
     * 
     * @return the message
     */
    String message() default "The first date must be before second date.";

    /**
     * Returns the groups.
     * 
     * @return the groups.
     */
    Class<?>[] groups() default { };

    /**
     * Returns the payload.
     * 
     * @return the payload
     */
    Class<? extends Payload>[] payload() default { };

    /**
     * Returns the First date field name.
     * 
     * @return the First date field name
     */
    String firstDate();

    /**
     * Returns the Second date field name.
     * 
     * @return the Second date field name
     */
    String secondDate();

    /**
     * The List of {@link Before}.
     */
    @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Before[] value();
    }
}
