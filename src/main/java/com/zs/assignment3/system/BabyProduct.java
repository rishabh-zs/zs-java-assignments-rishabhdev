package com.zs.assignment3.system;

/**
 * The type Baby product.
 */
public class BabyProduct extends Product {
    private final String recommendedAge;

    /**
     * Instantiates a new Baby product.
     *
     * @param id             the id
     * @param name           the name
     * @param price          the price
     * @param brand          the brand
     * @param recommendedAge the recommended age
     */
    public BabyProduct(String id, String name, double price, String brand, String recommendedAge) {
        super(id, name, price, brand);
        this.recommendedAge = recommendedAge;
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
     * @return a string representation of the baby product, including recommended age information
     */
    @Override
    public String toString() {
        String base = super.toString();
        String safeAge = recommendedAge == null ? "(no-age)" : recommendedAge;
        return base + " | Age: " + safeAge;
    }
}