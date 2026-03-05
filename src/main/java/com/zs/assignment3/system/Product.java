package com.zs.assignment3.system;

/**
 * The type Product.
 */
public abstract class Product {
    private String id;
    private String name;
    private double price;
    private String brand;

    /**
     * Instantiates a new Product.
     *
     * @param id    the id
     * @param name  the name
     * @param price the price
     * @param brand the brand
     */
    public Product(String id, String name, double price, String brand) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
    }

    /***
     * Is returnable boolean.
     * @return the boolean
     */
    public abstract boolean isReturnable();

    /***
     * Gets id.
     * @return the id
     */
    public String getId() {
        return id;
    }

    /***
     * Sets id.
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /***
     * Gets name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /***
     * Sets name.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /***
     * Gets price.
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /***
     * Sets price.
     * @param price the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /***
     * Gets brand.
     * @return the brand
     */
    public String getBrand() {
        return brand;
    }

    /***
     * Sets brand.
     * @param brand the brand
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /***
     * To string string.
     * @return a string representation of the product, including its name, brand, price, and returnability
     */
    @Override
    public String toString() {
        String safeName = name == null ? "(no-name)" : name;
        String safeBrand = brand == null ? "(no-brand)" : brand;
        String formattedPrice = String.format("%.2f", price);
        String returnable = isReturnable() ? "Yes" : "No";
        return String.format("%s | Brand: %s | Price: $%s | Returnable: %s", safeName, safeBrand, formattedPrice, returnable);
    }
}
