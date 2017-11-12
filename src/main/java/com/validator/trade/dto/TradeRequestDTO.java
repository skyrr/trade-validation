
package com.validator.trade.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;

import com.validator.trade.validators.annotations.Before;
import com.validator.trade.validators.annotations.Currency;
import com.validator.trade.validators.annotations.Customer;
import com.validator.trade.validators.annotations.DaysOfWeek;
import com.validator.trade.validators.groups.AmericanStyleChecks;
import com.validator.trade.validators.groups.VanillaOptionalTypeChecks;

import lombok.Builder;
import lombok.Getter;

/**
 * A DTO holding the Trade Request parameters.
 */
@Getter
@Builder
@Before.List({
    @Before(firstDate = "tradeDate", secondDate = "valueDate", message = "Value date cannot be before Trade date"),
    @Before(firstDate = "tradeDate", secondDate = "excerciseStartDate", message = "Exercise Start date cannot be before Value date", groups = AmericanStyleChecks.class),
    @Before(firstDate = "excerciseStartDate", secondDate = "expiryDate", message = "Expiry date cannot be before Exercise Start date", groups = AmericanStyleChecks.class),
    @Before(firstDate = "expiryDate", secondDate = "deliveryDate", message = "Delivery date cannot be before Expiry date", groups = {AmericanStyleChecks.class, VanillaOptionalTypeChecks.class}),
    @Before(firstDate = "premiumDate", secondDate = "deliveryDate", message = "Delivery date cannot be before Premium date", groups = {AmericanStyleChecks.class, VanillaOptionalTypeChecks.class})
})
public class TradeRequestDTO {

    /**
     * The Customer Name.
     */
    @Customer
    private String customer;

    /**
     * The pair of currencies for trade.
     */
    private String ccyPair;

    /**
     * The type of trade. Current values are SPOT, FORWARD and VANILLAOPTIONAL.
     */
    private String type;

    /**
     * The style for VANILLAOPTIONAL product types. Current values are AMERICAN and EUROPEAN.
     */
    private String style;

    /**
     * The Trade direction.
     */
    private String direction;

    /**
     * The trade strategy.
     */
    private String strategy;

    /**
     * The trade date.
     */
    private LocalDate tradeDate;

    /**
     * Amount in Currency 1.
     */
    private Double amount1;

    /**
     * Amount in Currency 2.
     */
    private Double amount2;

    /**
     * Rate of currency conversion.
     */
    private Double rate;

    /**
     * The value date.
     */
    @DaysOfWeek(value = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY}, message = "Value date cannot be on a weekend")
    private LocalDate valueDate;

    /**
     * The delivery date.
     */
    private LocalDate deliveryDate;

    /**
     * The expiry date of the trade.
     */
    private LocalDate expiryDate;

    /**
     * The exercise start date.
     */
    private LocalDate excerciseStartDate;

    /**
     * The Payment currency.
     */
    @Currency
    private String payCcy;

    /**
     * The premium amount.
     */
    private Double premium;

    /**
     * The Premium currency.
     */
    @Currency
    private String premiumCcy;

    /**
     * The premium type.
     */
    private String premiumType;

    /**
     * The premium date.
     */
    private LocalDate premiumDate;

    /**
     * The legal Entity.
     */
    private String legalEntity;

    /**
     * The trader name.
     */
    private String trader;
}
