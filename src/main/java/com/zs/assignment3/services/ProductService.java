package com.zs.assignment3.services;

import com.zs.assignment3.repositories.InMemoryProductRepository;
import com.zs.assignment3.repositories.ProductRepository;
import com.zs.assignment3.system.Product;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * The type Product service.
 */
public class ProductService {
    private final ProductRepository repository = new InMemoryProductRepository();

    /***
     * Fetch all products list.
     * @return the list
     */
    public List<Product> fetchAllProducts() {
        return repository.findAll();
    }

    /***
     * Add product.
     * @param p the p
     */
    public void addProduct(Product p) {
        if (p != null){
            repository.save(p);
        }
    }

    /***
     * Remove product by id boolean.
     * @param id the id
     * @return the boolean
     */
    public boolean removeProductById(String id) {
        return id != null && repository.deleteById(id);
    }

    /***
     * Search by name list.
     * @param name the name
     * @return the list
     */
    public List<Product> searchByName(String name) {
        String query = (name == null) ? "" : name.toLowerCase(Locale.ROOT).trim();
        return repository
                .findAll()
                .stream()
                .filter(p -> p.getName().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    /***
     * Find by id product.
     * @param id the id
     * @return the product
     */
    public Product findById(String id) {
        if (id == null) {
            return null;
        }
        for (Product p : repository.findAll()) {
            if (id.equalsIgnoreCase(p.getId())) return p;
        }
        return null;
    }
}