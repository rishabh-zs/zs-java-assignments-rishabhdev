package com.zs.assignment12.dao;

import com.zs.assignment12.exception.ProductNotFoundException;
import com.zs.assignment12.model.Product;
import com.zs.assignment12.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDaoJdbcImpl implements ProductDao {
    private static final Logger log = LoggerUtil.getLogger(ProductDaoJdbcImpl.class);
    private final JdbcTemplate jdbcTemplate;

    // Constructor Injection
    public ProductDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void CreateProductTable(){
        log.debug("Executing SQL to create product table");

        final String DROP_PRODUCT_TABLE_SQL = "DROP TABLE IF EXISTS product";
        final String CREATE_PRODUCT_TABLE_SQL = "CREATE TABLE product (" +
                "id INTEGER PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "price DOUBLE PRECISION NOT NULL CHECK (price >= 0), " +
                "category_id INTEGER NOT NULL, " +
                "CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE" +
                ")";

        try {
            jdbcTemplate.execute(DROP_PRODUCT_TABLE_SQL);
            jdbcTemplate.execute(CREATE_PRODUCT_TABLE_SQL);
        } catch (DataAccessException ex) {
            log.error("Failed to create product table", ex);
            throw ex;
        }
    }


    @Override
    public List<Product> findAllProducts() {
        log.debug("Executing SQL to fetch all products");

        final String FIND_ALL_PRODUCTS_SQL = "SELECT id, name, price, category_id FROM product ORDER BY id";

        return jdbcTemplate.query(FIND_ALL_PRODUCTS_SQL, (rs, rowNum) -> {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setCategoryId(rs.getInt("category_id"));
            return product;
        });
    }

    @Override
    public void deleteProductById(Long id) {
        log.debug("Executing SQL to delete product by id {}", id);

        final String DELETE_PRODUCT_BY_ID_SQL = "DELETE FROM product WHERE id = ?";

        int deletedRows = jdbcTemplate.update(DELETE_PRODUCT_BY_ID_SQL, id);
        if (deletedRows == 0) {
            throw new ProductNotFoundException("Product not found for id: " + id);
        }

    }

    @Override
    public void addProduct(Product product) {
        log.debug("Executing SQL to add product with id: {}", product.getId());

        final String INSERT_PRODUCT_SQL = "INSERT INTO product (id, name, price, category_id) VALUES (?, ?, ?, ?)";

        int insertedRows = jdbcTemplate.update(
                INSERT_PRODUCT_SQL,
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategoryId()
        );
        if (insertedRows != 1) {
            throw new IllegalStateException("Unable to insert product with id: " + product.getId());
        }

    }
}
