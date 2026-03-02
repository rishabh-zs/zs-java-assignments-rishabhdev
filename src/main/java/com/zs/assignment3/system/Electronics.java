package com.zs.assignment3.system;

/**
 * The type Electronics.
 */
public class Electronics extends Product {
    private final int warrantyMonths;

    /**
     * Instantiates a new Electronics.
     *
     * @param id             the id
     * @param name           the name
     * @param price          the price
     * @param brand          the brand
     * @param warrantyMonths the warranty months
     */
    public Electronics(String id, String name, double price,String brand, int warrantyMonths) {
        super(id, name, price, brand);
        this.warrantyMonths = warrantyMonths;
    }

    /***
     * Is returnable boolean.
     * @return true if the product is returnable, false otherwise
     */
    @Override
    public boolean isReturnable() {
        return true;
    }

    /***
     * To string string.
     * @return a string representation of the electronics product, including warranty information
     */
    @Override
    public String toString() {
        String base = super.toString();
        return base + " | Warranty: " + warrantyMonths + " months";
    }
}