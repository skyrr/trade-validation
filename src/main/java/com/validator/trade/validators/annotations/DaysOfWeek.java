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
import java.time.DayOfWeek;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.validator.trade.validators.DaysOfWeekValidator;

/**
 * The annotation for {@link DaysOfWeekValidator}.
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {DaysOfWeekValidator.class})
public @interface DaysOfWeek {

    /**
     * Returns the default message.
     * 
     * @return the default message
     */
    String message() default "Date does not fall on Given days.";

    /**
     * Returns the group names.
     * 
     * @return the group names
     */
    Class<?>[] groups() default { };

    /**
     * Returns the payload.
     * 
     * @return the payload.
     */
    Class<? extends Payload>[] payload() default { };

    /**
     * The list of days of week allowed.
     * @return
     */
    DayOfWeek[] value();
}
