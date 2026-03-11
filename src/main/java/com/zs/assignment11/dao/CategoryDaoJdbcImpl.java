package com.zs.assignment11.dao;

import com.zs.assignment11.exception.CategoryNotFoundException;
import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.util.LoggerUtil;
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
        log.debug("Checking whether category table already exists");

        final String CHECK_CATEGORY_TABLE_EXISTS_SQL =
                "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'category')";

        final String CREATE_CATEGORY_TABLE_SQL = "CREATE TABLE IF NOT EXISTS category (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL UNIQUE" +
                ")";

        try {
            Boolean tableExists = jdbcTemplate.queryForObject(CHECK_CATEGORY_TABLE_EXISTS_SQL, Boolean.class);
            if (Boolean.TRUE.equals(tableExists)) {
                log.info("Category table already exists, skipping create");
                return;
            }

            jdbcTemplate.execute(CREATE_CATEGORY_TABLE_SQL);
            log.info("Category table created successfully");
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

        final String INSERT_CATEGORY_SQL = "INSERT INTO category (name) VALUES (?)";

        int insertedRows = jdbcTemplate.update(INSERT_CATEGORY_SQL, category.getName());
        if (insertedRows != 1) {
            throw new IllegalStateException("Unable to insert category: " + category.getName());
        }
    }

    @Override
    public void deleteCategory(Long id){
        log.debug("Executing SQL to delete category");
        final String DELETE_CATEGORY_SQL = "DELETE FROM category WHERE id = ?";

        int deletedRows=jdbcTemplate.update(DELETE_CATEGORY_SQL, id);
        if(deletedRows!=1){
            throw new IllegalStateException("Unable to delete category: " + id);
        }
    }

}
