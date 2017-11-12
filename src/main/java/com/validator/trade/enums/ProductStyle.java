package com.validator.trade.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * The Product Style enum.
 */
public enum ProductStyle {

    /**
     * American.
     */
    AMERICAN("AMERICAN"),

    /**
     * European.
     */
    EUROPEAN("EUROPEAN");

    /**
     * The product style name.
     */
    @Getter
    private String productStyleName;

    /**
     * The map of product style vs {@link ProductStyle}.
     */
    private static Map<String, ProductStyle> productStyleMap;

    static {
        productStyleMap = new HashMap<>();
        for (ProductStyle productStyle : ProductStyle.values()) {
            productStyleMap.put(productStyle.productStyleName, productStyle);
        }
    }

    /**
     * Initializes the product style name.
     * 
     * @param productStyleName the product style name
     */
    private ProductStyle(final String productStyleName) {
        this.productStyleName = productStyleName;
    }

    /**
     * Finds and returns the {@link ProductStyle} by the product style name.
     * 
     * @param productStyleName the product style name
     * @return the ProductStyle
     */
    public static ProductStyle getByProductStyleName(final String productStyleName) {
        return productStyleMap.get(productStyleName);
    }
}
