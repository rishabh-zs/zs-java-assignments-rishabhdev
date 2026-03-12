package com.zs.assignment12.dao;

import com.zs.assignment12.model.Product;
import java.util.List;

public interface ProductDao {
    void CreateProductTable();
    List<Product> findAllProducts();
    void deleteProductById(Long id);
    void addProduct(Product product);
}
