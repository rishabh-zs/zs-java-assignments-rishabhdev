package com.zs.assignment3.system;

public class Grocery extends Product {
    private String expiryDate;

    public Grocery(String id, String name, double price,String brand ,String expiryDate) {
        super(id, name, price,brand);
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean isReturnable() {
        return true;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    @Override
    public String toString() {
        String base = super.toString();
        String safeExpiry = expiryDate == null ? "(no-expiry)" : expiryDate;
        return base + " | Expiry: " + safeExpiry;
    }
}
