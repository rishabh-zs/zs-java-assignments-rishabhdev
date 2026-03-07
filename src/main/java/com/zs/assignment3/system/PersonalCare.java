package com.zs.assignment3.system;

/**
 * The type Personal care.
 */
public class PersonalCare extends Product {
    /**
     * Instantiates a new Personal care.
     *
     * @param id    the id
     * @param name  the name
     * @param price the price
     * @param brand the brand
     */
    public PersonalCare(String id, String name, double price, String brand) {
        super(id, name, price, brand);
    }

    /***
     * Is returnable boolean.
     * @return false, as personal care products are non-returnable
     */
    @Override
    public boolean isReturnable() {
        return false;
    }

    /***
     * To string string.
     * @return a string representation of the personal care product, indicating it is non-returnable
     */
    @Override
    public String toString() {
        return super.toString();
    }
}