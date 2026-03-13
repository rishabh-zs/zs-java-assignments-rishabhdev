package com.zs.assignment11.dao;

import com.zs.assignment11.exception.ProductNotFoundException;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * The type Product dao jdbc.
 */
@Repository
public class ProductDaoJdbcImpl implements ProductDao {
    private static final Logger log = LoggerUtil.getLogger(ProductDaoJdbcImpl.class);
    private final JdbcTemplate jdbcTemplate;

    /**
     * Instantiates a new Product dao jdbc.
     *
     * @param jdbcTemplate the jdbc template
     */
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
            Integer id = rs.getInt("id");
            String name = rs.getString("name");
            Double price = rs.getDouble("price");
            Integer catId = rs.getInt("category_id");
            return new Product(id, name, price, catId);
        });
    }

    @Override
    public Product addProduct(Product product) {
        log.debug("Executing SQL to insert product");

        final String INSERT_PRODUCT_SQL =
                "INSERT INTO product (name, price, category_id) VALUES (?, ?, ?) RETURNING id";

        Integer productId = jdbcTemplate.queryForObject(
                INSERT_PRODUCT_SQL,
                Integer.class,
                product.getName(),
                product.getPrice(),
                product.getCategoryId()
        );
        if (productId == null) {
            throw new IllegalStateException("Unable to insert product: " + product.getName());
        }

        return new Product(productId, product.getName(), product.getPrice(), product.getCategoryId());
    }

    @Override
    public Product deleteProduct(Long id) {
        log.debug("Executing SQL to delete product");

        final String FIND_PRODUCT_BY_ID_SQL = "SELECT id, name, price, category_id FROM product WHERE id = ?";
        final String DELETE_PRODUCT_SQL = "DELETE FROM product WHERE id = ?";

        Product deletedProduct;
        try {
            deletedProduct = jdbcTemplate.queryForObject(
                    FIND_PRODUCT_BY_ID_SQL,
                    (rs, rowNum) -> new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("category_id")
                    ),
                    id
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new ProductNotFoundException("Product not found for id: " + id);
        }

        int deletedRows = jdbcTemplate.update(DELETE_PRODUCT_SQL, id);
        if (deletedRows != 1) {
            throw new IllegalStateException("Unable to delete product: " + id);
        }
        return deletedProduct;
    }

    @Override
    public Product updateProduct(Product product) {
        log.debug("Executing SQL to update product");

        final String FIND_PRODUCT_BY_ID_SQL = "SELECT id, name, price, category_id FROM product WHERE id = ?";
        final String UPDATE_PRODUCT_SQL = "UPDATE product SET name = ?, price = ? WHERE id = ?";

        Product existing;
        try {
            existing = jdbcTemplate.queryForObject(
                    FIND_PRODUCT_BY_ID_SQL,
                    (rs, rowNum) -> new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("category_id")
                    ),
                    product.getId()
            );
        } catch (EmptyResultDataAccessException ex) {
            throw new ProductNotFoundException("Product not found for id: " + product.getId());
        }

        int updatedRows = jdbcTemplate.update(UPDATE_PRODUCT_SQL, product.getName(), product.getPrice(), product.getId());
        if (updatedRows != 1) {
            throw new IllegalStateException("Unable to update product with id: " + product.getId());
        }

        return new Product(product.getId(), product.getName(), product.getPrice(), existing.getCategoryId());
    }

}
