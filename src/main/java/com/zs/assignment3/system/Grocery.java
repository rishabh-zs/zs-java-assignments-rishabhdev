package com.zs.assignment3.system;

/**
 * The type Grocery.
 */
public class Grocery extends Product {
    private final String expiryDate;

    /**
     * Instantiates a new Grocery.
     *
     * @param id         the id
     * @param name       the name
     * @param price      the price
     * @param brand      the brand
     * @param expiryDate the expiry date
     */
    public Grocery(String id, String name, double price,String brand ,String expiryDate) {
        super(id, name, price,brand);
        this.expiryDate = expiryDate;
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
     * @return a string representation of the grocery product, including expiry date information
     */
    @Override
    public String toString() {
        String base = super.toString();
        String safeExpiry = expiryDate == null ? "(no-expiry)" : expiryDate;
        return base + " | Expiry: " + safeExpiry;
    }
}
