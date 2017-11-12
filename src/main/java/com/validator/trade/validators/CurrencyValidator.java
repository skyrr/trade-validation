package com.validator.trade.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.validator.trade.validators.annotations.Currency;

/**
 * Validates the Currency is a valid value.
 */
public class CurrencyValidator implements ConstraintValidator<Currency, String> {

    @Override
    public void initialize(final Currency constraintAnnotation) {
    }

    /**
     * Valid if Null or a correct currency code.
     * Not Valid otherwise.
     */
    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            return java.util.Currency.getInstance(value) != null;
        } catch (Exception e) {
            return false;
        }
    }

}
