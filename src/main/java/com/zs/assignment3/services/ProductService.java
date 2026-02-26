package com.zs.assignment3.services;

import com.zs.assignment3.repositories.InMemoryProductRepository;
import com.zs.assignment3.repositories.ProductRepository;
import com.zs.assignment3.system.Product;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProductService {
    private final ProductRepository repository = new InMemoryProductRepository();

    public List<Product> fetchAllProducts() {
        return repository.findAll();
    }

    public void addProduct(Product p) {
        if (p != null) repository.save(p);
    }

    public boolean removeProductById(String id) {
        return id != null && repository.deleteById(id);
    }

    public List<Product> searchByName(String name) {
        String query = (name == null) ? "" : name.toLowerCase(Locale.ROOT).trim();
        return repository.findAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

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