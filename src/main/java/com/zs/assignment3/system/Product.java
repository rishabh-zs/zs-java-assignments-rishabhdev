package com.zs.assignment3.system;

public abstract class Product {
    private String id;
    private String name;
    private double price;
    private String brand;

    public Product(String id, String name, double price, String brand) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.brand = brand;
    }

    // Abstract method to be implemented by subclasses
    public abstract boolean isReturnable();

    // Getters and Setters (Encapsulation)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
}

