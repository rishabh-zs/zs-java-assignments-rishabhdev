package com.zs.assignment11.service;

import com.zs.assignment11.dao.ProductDao;
import com.zs.assignment11.exception.CannotCreateProductTableException;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service handling Product business logic.
 */
@Service
public class ProductService {
    private List<Product> products=new ArrayList<>();
    private static final Logger log = LoggerUtil.getLogger(ProductService.class);
    private final ProductDao productDao;

    // Constructor Injection
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }


    public void CreateProductTable(){
        log.info("Request received to create product table");
        try {
            productDao.CreateProductTable();
        } catch (DataAccessException ex) {
            throw new CannotCreateProductTableException("Failed to Create Product table.", ex);
        }
    }

    public List<Product> getAllProducts() {
        log.info("Request received to fetch all products");
        try{
            products=productDao.findAllProducts();
        }catch (DataAccessException ex) {
            throw new IllegalArgumentException("Failed to fetch all products", ex);
        }
        return products;
    }

}
