package com.validator.trade.validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;

import com.validator.trade.validators.annotations.Before;
/**
 * Validates that the fist date is before the second date.
 */
public class BeforeValidator implements ConstraintValidator<Before, Object> {

    /**
     * The first date field name.
     */
    private String firstDateFieldName;

    /**
     * The second date field name.
     */
    private String secondDateFieldName;

    @Override
    public void initialize(final Before constraintAnnotation) {
        firstDateFieldName = constraintAnnotation.firstDate();
        secondDateFieldName = constraintAnnotation.secondDate();
    }

    /**
     * Valid if the first date is before second date.
     * Not Valid if any date is null or not found.
     */
    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final String firstDateString = BeanUtils.getProperty(value, firstDateFieldName);
            final String secondDateString = BeanUtils.getProperty(value, secondDateFieldName);

            if (firstDateString == null || secondDateString == null) {
                return false;
            }

            final LocalDate firstDate = LocalDate.parse(firstDateString, DateTimeFormatter.ISO_DATE);
            final LocalDate secondDate = LocalDate.parse(secondDateString, DateTimeFormatter.ISO_DATE);

            if (firstDate.isBefore(secondDate)) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
