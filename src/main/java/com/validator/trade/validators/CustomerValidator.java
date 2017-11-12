package com.validator.trade.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.validator.trade.validators.annotations.Customer;

/**
 * Validates an existing customer.
 */
public class CustomerValidator implements ConstraintValidator<Customer, String> {

    @Override
    public void initialize(final Customer constraintAnnotation) {
    }

    /**
     * Valid if the customer exists in the records.
     */
    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return com.validator.trade.enums.Customer.getByCustomerName(value) != null;
    }

}
