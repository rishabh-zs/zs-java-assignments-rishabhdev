package com.zs.assignment3.services;

import com.zs.assignment3.system.BabyProduct;
import com.zs.assignment3.system.Electronics;
import com.zs.assignment3.system.Grocery;
import com.zs.assignment3.system.PersonalCare;
import com.zs.assignment3.system.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductService {
    // Internal mutable list of products used by the application
    private final List<Product> products = new ArrayList<>();

    public ProductService() {
        // Initialize with sample data
        // 1. Grocery
        products.add(new Grocery("G1", "Milk", 2.5, "Amul", "2026-03-01"));
        products.add(new Grocery("G2", "Bread", 1.5, "Harvest", "2026-02-25"));
        products.add(new Grocery("G3", "Eggs", 3.0, "FarmFresh", "2026-03-05"));
        products.add(new Grocery("G4", "Butter", 4.0, "Amul", "2026-05-01"));
        products.add(new Grocery("G5", "Cereal", 5.5, "Kelloggs", "2027-01-01"));

        // 2. Electronics
        products.add(new Electronics("E1", "iPhone 15", 999.0, "Apple", 12));
        products.add(new Electronics("E2", "S24 Ultra", 1199.0, "Samsung", 24));
        products.add(new Electronics("E3", "WH-1000XM5", 350.0, "Sony", 12));
        products.add(new Electronics("E4", "MacBook Air", 1299.0, "Apple", 12));
        products.add(new Electronics("E5", "Kindle", 139.0, "Amazon", 6));

        // 3. Personal Care (Non-Returnable)
        products.add(new PersonalCare("P1", "Shampoo", 15.0, "Dove"));
        products.add(new PersonalCare("P2", "Face Wash", 10.0, "Neutrogena"));
        products.add(new PersonalCare("P3", "Sunscreen", 20.0, "La Roche"));
        products.add(new PersonalCare("P4", "Moisturizer", 18.0, "CeraVe"));
        products.add(new PersonalCare("P5", "Toothpaste", 5.0, "Colgate"));

        // 4. Baby Products
        products.add(new BabyProduct("B1", "Diapers", 25.0, "Pampers", "0-6 months"));
        products.add(new BabyProduct("B2", "Baby Wipes", 8.0, "Huggies", "All ages"));
        products.add(new BabyProduct("B3", "Stroller", 150.0, "Graco", "6-36 months"));
        products.add(new BabyProduct("B4", "Baby Bottle", 12.0, "Philips", "0-12 months"));
        products.add(new BabyProduct("B5", "Teether", 7.0, "FisherPrice", "3-12 months"));
    }

    public List<Product> fetchAllProducts() {
        return new ArrayList<>(products);
    }

    public void addProduct(Product p) {
        if (p != null) {
            products.add(p);
        }
    }

    public boolean removeProductById(String id) {
        if (id == null){
            return false;
        }
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (id.equalsIgnoreCase(p.getId())) {
                products.remove(i);
                return true;
            }
        }
        return false;
    }

    public Product findById(String id) {
        if (id == null) {
            return null;
        }
        for (Product p : products) {
            if (id.equalsIgnoreCase(p.getId())) return p;
        }
        return null;
    }

    public List<Product> searchByName(String name) {
        List<Product> result = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) return result;
        String q = name.toLowerCase(Locale.ROOT);
        for (Product p : products) {
            if (p.getName() != null && p.getName().toLowerCase(Locale.ROOT).contains(q)) {
                result.add(p);
            }
        }
        return result;
    }
}