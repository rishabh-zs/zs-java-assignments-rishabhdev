package com.zs.assignment11.dao;

import com.zs.assignment11.model.Product;
import java.util.List;

public interface ProductDao {
    void CreateProductTable();
    List<Product> findAllProducts();
    Product addProduct(Product product);
    Product deleteProduct(Long id);
    Product updateProduct(Product product);
}
