package com.zs.assignment12.dao;

import com.zs.assignment12.exception.CategoryNotFoundException;
import com.zs.assignment12.model.Category;
import com.zs.assignment12.model.Product;
import com.zs.assignment12.util.LoggerUtil;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * JDBC Implementation for Category Data Access.
 */
@Repository
public class CategoryDaoJdbcImpl implements CategoryDao {
    private static final Logger log = LoggerUtil.getLogger(CategoryDaoJdbcImpl.class);
    private final JdbcTemplate jdbcTemplate;

    // Constructor Injection
    public CategoryDaoJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void CreateCategoryTable(){
        log.debug("Executing SQL to create category table");

        final String DROP_CATEGORY_TABLE_SQL = "DROP TABLE IF EXISTS category CASCADE";
        final String CREATE_CATEGORY_TABLE_SQL = "CREATE TABLE category (" +
                "id INTEGER PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL" +
                ")";

        try {
            jdbcTemplate.execute(DROP_CATEGORY_TABLE_SQL);
            jdbcTemplate.execute(CREATE_CATEGORY_TABLE_SQL);
        } catch (DataAccessException ex) {
            log.error("Failed to create category table", ex);
            throw ex;
        }
    }

    @Override
    public List<Category> findAllCategories() {
        log.debug("Executing SQL to fetch all categories");

        final String FIND_ALL_CATEGORIES_SQL = "SELECT id, name FROM category ORDER BY id";

        return jdbcTemplate.query(FIND_ALL_CATEGORIES_SQL,
                (rs, rowNum) -> new Category(rs.getInt("id"), rs.getString("name")));
    }

    @Override
    public List<Product> findAllProductsByCategoryId(Long categoryId) {
        log.debug("Executing SQL to fetch all products by category id");

        final String CHECK_CATEGORY_EXISTS_SQL = "SELECT COUNT(1) FROM category WHERE id = ?";
        final String FIND_PRODUCTS_BY_CATEGORY_ID_SQL =
                "SELECT id, name, price, category_id FROM product WHERE category_id = ? ORDER BY id";

        Integer categoryCount = jdbcTemplate.queryForObject(CHECK_CATEGORY_EXISTS_SQL, Integer.class, categoryId);
        if (categoryCount == null || categoryCount == 0) {
            throw new CategoryNotFoundException("Category not found for id: " + categoryId);
        }

        return jdbcTemplate.query(FIND_PRODUCTS_BY_CATEGORY_ID_SQL, (rs, rowNum) -> {
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            product.setCategoryId(rs.getInt("category_id"));
            return product;
        }, categoryId);
    }

    @Override
    public void addCategory(Category category) {
        log.debug("Executing SQL to insert category");

        final String INSERT_CATEGORY_SQL = "INSERT INTO category (id, name) VALUES (?, ?)";

        int insertedRows = jdbcTemplate.update(INSERT_CATEGORY_SQL, category.getId(), category.getName());
        if (insertedRows != 1) {
            throw new IllegalStateException("Unable to insert category with id: " + category.getId());
        }
    }
}
