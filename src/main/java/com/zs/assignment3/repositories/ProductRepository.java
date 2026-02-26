package com.zs.assignment3.repositories;

import com.zs.assignment3.system.Product;
import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
    void save(Product product);
    boolean deleteById(String id);
    Product findById(String id);
}