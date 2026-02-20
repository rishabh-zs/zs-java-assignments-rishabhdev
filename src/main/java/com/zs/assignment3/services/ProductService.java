package com.zs.assignment3.services;

import com.zs.assignment3.system.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    public List<Product> fetchAllProducts() {
        List<Product> products = new ArrayList<>();

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


        return products;
    }
}