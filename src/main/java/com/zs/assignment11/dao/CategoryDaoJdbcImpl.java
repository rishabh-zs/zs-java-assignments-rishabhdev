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

    /**
     * Instantiates a new Category dao jdbc.
     *
     * @param jdbcTemplate the jdbc template
     */
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
    public Category addCategory(Category category) {
        log.debug("Executing SQL to insert category");

        final String INSERT_CATEGORY_SQL = "INSERT INTO category (name) VALUES (?)";

        int insertedRows = jdbcTemplate.update(INSERT_CATEGORY_SQL, category.getName());
        if (insertedRows != 1) {
            throw new IllegalStateException("Unable to insert category: " + category.getName());
        }
        return new Category(category.getId(), category.getName());

    }

    @Override
    public Category deleteCategory(Long id) {
        log.debug("Executing SQL to delete category");
        final String FIND_CATEGORY_BY_ID_SQL = "SELECT id, name FROM category WHERE id = ?";
        final String DELETE_CATEGORY_SQL = "DELETE FROM category WHERE id = ?";

        List<Category> categories = jdbcTemplate.query(
                FIND_CATEGORY_BY_ID_SQL,
                (rs, rowNum) -> new Category(rs.getInt("id"), rs.getString("name")),
                id
        );

        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("Category not found for id: " + id);
        }

        Category deletedCategory = categories.get(0);
        int deletedRows = jdbcTemplate.update(DELETE_CATEGORY_SQL, id);
        if (deletedRows != 1) {
            throw new IllegalStateException("Unable to delete category: " + id);
        }

        return deletedCategory;
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
            Integer id=rs.getInt("id");
            String name=rs.getString("name");
            Double price=rs.getDouble("price");
            Integer catId=rs.getInt("category_id");
            Product product=new Product(id,name,price,catId);
            return product;
        }, categoryId);
    }

    @Override
    public Category updateCategory(Category category) {
        log.debug("Executing SQL to update category");

        final String FIND_CATEGORY_BY_ID_SQL = "SELECT id, name FROM category WHERE id = ?";
        final String UPDATE_CATEGORY_SQL = "UPDATE category SET name = ? WHERE id = ?";

        List<Category> existing = jdbcTemplate.query(
                FIND_CATEGORY_BY_ID_SQL,
                (rs, rowNum) -> new Category(rs.getInt("id"), rs.getString("name")),
                category.getId()
        );

        if (existing.isEmpty()) {
            throw new CategoryNotFoundException("Category not found for id: " + category.getId());
        }

        int updatedRows = jdbcTemplate.update(UPDATE_CATEGORY_SQL, category.getName(), category.getId());
        if (updatedRows != 1) {
            throw new IllegalStateException("Unable to update category with id: " + category.getId());
        }

        return new Category(category.getId(), category.getName());
    }
}
