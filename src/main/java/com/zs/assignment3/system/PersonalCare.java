package com.zs.assignment3.system;

public class PersonalCare extends Product {
    public PersonalCare(String id, String name, double price, String brand) {
        super(id, name, price, brand);
    }

    @Override
    public boolean isReturnable() {
        return false;
    }

    @Override
    public String toString() {
        // Personal care is non-returnable;
        return super.toString();
    }
}