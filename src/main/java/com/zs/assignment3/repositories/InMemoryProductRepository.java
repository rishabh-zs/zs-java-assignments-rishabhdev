package com.zs.assignment3.repositories;

import com.zs.assignment3.system.BabyProduct;
import com.zs.assignment3.system.Electronics;
import com.zs.assignment3.system.Grocery;
import com.zs.assignment3.system.PersonalCare;
import com.zs.assignment3.system.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * The type In memory product repository.
 */
public class InMemoryProductRepository implements ProductRepository {
    private final List<Product> products = new ArrayList<>();

    /**
     * Instantiates a new In memory product repository.
     * This constructor initializes the in-memory repository with a predefined set of products across various categories:
     * 1. Grocery.
     * 2. Electronics.
     * 3. Personal Care (Non-Returnable).
     * 4. Baby Products.
     */
    public InMemoryProductRepository() {
        products.add(new Grocery("G1", "Milk", 2.5, "Amul", "2026-03-01"));
        products.add(new Grocery("G2", "Bread", 1.5, "Harvest", "2026-02-25"));
        products.add(new Grocery("G3", "Eggs", 3.0, "FarmFresh", "2026-03-05"));
        products.add(new Grocery("G4", "Butter", 4.0, "Amul", "2026-05-01"));
        products.add(new Grocery("G5", "Cereal", 5.5, "Kelloggs", "2027-01-01"));

        products.add(new Electronics("E1", "iPhone 15", 999.0, "Apple", 12));
        products.add(new Electronics("E2", "S24 Ultra", 1199.0, "Samsung", 24));
        products.add(new Electronics("E3", "WH-1000XM5", 350.0, "Sony", 12));
        products.add(new Electronics("E4", "MacBook Air", 1299.0, "Apple", 12));
        products.add(new Electronics("E5", "Kindle", 139.0, "Amazon", 6));

        products.add(new PersonalCare("P1", "Shampoo", 15.0, "Dove"));
        products.add(new PersonalCare("P2", "Face Wash", 10.0, "Neutrogena"));
        products.add(new PersonalCare("P3", "Sunscreen", 20.0, "La Roche"));
        products.add(new PersonalCare("P4", "Moisturizer", 18.0, "CeraVe"));
        products.add(new PersonalCare("P5", "Toothpaste", 5.0, "Colgate"));

        products.add(new BabyProduct("B1", "Diapers", 25.0, "Pampers", "0-6 months"));
        products.add(new BabyProduct("B2", "Baby Wipes", 8.0, "Huggies", "All ages"));
        products.add(new BabyProduct("B3", "Stroller", 150.0, "Graco", "6-36 months"));
        products.add(new BabyProduct("B4", "Baby Bottle", 12.0, "Philips", "0-12 months"));
        products.add(new BabyProduct("B5", "Teether", 7.0, "FisherPrice", "3-12 months"));

    }

    /***
     * Find all list.
     * @return all products in the repository.
     */
    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    /***
     * Save.
     * @param product the product
     */
    @Override
    public void save(Product product) {
        products.add(product);
    }

    /***
     * Delete by id boolean.
     * @param id the id
     * @return the boolean
     */
    @Override
    public boolean deleteById(String id) {
        return products
                .removeIf(p -> p.getId().equalsIgnoreCase(id));
    }

    /***
     * Find by id product.
     * @param id the id
     * @return the product
     */
    @Override
    public Product findById(String id) {
        return products
                .stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
}