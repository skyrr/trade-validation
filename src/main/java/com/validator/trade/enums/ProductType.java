package com.validator.trade.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * The Product Type enum.
 */
public enum ProductType {

    /**
     * Spot.
     */
    SPOT("Spot"),

    /**
     * Forward.
     */
    FORWARD("Forward"),

    /**
     * Optional.
     */
    OPTIONAL("VanillaOption");

    /**
     * The product type name.
     */
    @Getter
    private String productTypeName;

    /**
     * The map of product type name vs {@link ProductType}.
     */
    private static Map<String, ProductType> productTypesMap;

    static {
        productTypesMap = new HashMap<>();
        for (ProductType productType : ProductType.values()) {
            productTypesMap.put(productType.productTypeName, productType);
        }
    }

    /**
     * Initializes the {@link ProductType}.
     * 
     * @param productTypeName the product type name
     */
    private ProductType(final String productTypeName) {
        this.productTypeName = productTypeName;
    }

    /**
     * Finds and Returns the {@link ProductType} by the product type name.
     * 
     * @param productTypeName the product type name
     * @return the ProductType
     */
    public static ProductType getByProductTypeName(final String productTypeName) {
        return productTypesMap.get(productTypeName);
    }
}
