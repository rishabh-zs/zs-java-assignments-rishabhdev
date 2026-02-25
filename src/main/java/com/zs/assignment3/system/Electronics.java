package com.zs.assignment3.system;

public class Electronics extends Product {
    private int warrantyMonths;

    public Electronics(String id, String name, double price,String brand, int warrantyMonths) {
        super(id, name, price, brand);
        this.warrantyMonths = warrantyMonths;
    }

    @Override
    public boolean isReturnable() {
        return true;
    }

    public int getWarrantyMonths(){
        return warrantyMonths;
    }

    @Override
    public String toString() {
        String base = super.toString();
        return base + " | Warranty: " + warrantyMonths + " months";
    }
}