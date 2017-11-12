package com.validator.trade.validators;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.validator.trade.validators.annotations.DaysOfWeek;

/**
 * Validates that the date provided falls on the provided days of week.
 */
public class DaysOfWeekValidator implements ConstraintValidator<DaysOfWeek, LocalDate> {

    /**
     * The days of week allowed.
     */
    private Set<DayOfWeek> daysOfWeekAllowed;

    @Override
    public void initialize(final DaysOfWeek constraintAnnotation) {
        daysOfWeekAllowed = new HashSet<>();
        daysOfWeekAllowed.addAll(Arrays.asList(constraintAnnotation.value()));
    }

    /**
     * Valid if date falls on the provided days.
     * Not Valid if Null.
     */
    @Override
    public boolean isValid(final LocalDate value, final ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return daysOfWeekAllowed.contains(value.getDayOfWeek());
    }

}
