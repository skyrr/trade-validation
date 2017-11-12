package com.validator.trade.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * The Customer enum.
 */
public enum Customer {

    /**
     * PLUTO1.
     */
    PLUTO1("PLUTO1"),

    /**
     * PLUTO2
     */
    PLUTO2("PLUTO2");

    /**
     * The customer name.
     */
    @Getter
    private String customerName;

    /**
     * Map of customer name vs {@link Customer}.
     */
    private static Map<String, Customer> customersMap;

    static {
        customersMap = new HashMap<>();
        for (Customer customer : Customer.values()) {
            customersMap.put(customer.customerName, customer);
        }
    }

    /**
     * Initializes a Customer.
     * 
     * @param customerName the customer name
     */
    private Customer(final String customerName) {
        this.customerName = customerName;
    }

    
    /**
     * Finds and returns the {@link Customer} by customer name.
     * 
     * @param customerName the customer name to be searched
     * @return the Customer
     */
    public static Customer getByCustomerName(final String customerName) {
        return customersMap.get(customerName);
    }
}
