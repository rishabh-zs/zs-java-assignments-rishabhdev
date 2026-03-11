package com.zs.assignment11.dao;

import com.zs.assignment11.exception.ProductNotFoundException;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.util.LoggerUtil;
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
        log.debug("Checking whether product table already exists");

        final String CHECK_PRODUCT_TABLE_EXISTS_SQL =
                "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'product')";

        final String CREATE_PRODUCT_TABLE_SQL = "CREATE TABLE IF NOT EXISTS product (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL UNIQUE, " +
                "price DOUBLE PRECISION NOT NULL CHECK (price >= 0), " +
                "category_id INTEGER NOT NULL, " +
                "CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE" +
                ")";

        try {
            Boolean tableExists = jdbcTemplate.queryForObject(CHECK_PRODUCT_TABLE_EXISTS_SQL, Boolean.class);
            if (Boolean.TRUE.equals(tableExists)) {
                log.info("Product table already exists, skipping create");
                return;
            }

            jdbcTemplate.execute(CREATE_PRODUCT_TABLE_SQL);
            log.info("Product table created successfully");
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
        log.debug("Executing SQL to add product: {}", product.getName());

        final String INSERT_PRODUCT_SQL = "INSERT INTO product (name, price, category_id) VALUES (?, ?, ?)";

        int insertedRows = jdbcTemplate.update(
                INSERT_PRODUCT_SQL,
                product.getName(),
                product.getPrice(),
                product.getCategoryId()
        );
        if (insertedRows != 1) {
            throw new IllegalStateException("Unable to insert product: " + product.getName());
        }

    }
}
