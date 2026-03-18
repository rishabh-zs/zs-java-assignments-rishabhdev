package com.zs.assignment11.dao;

import com.zs.assignment11.exception.CategoryNotFoundException;
import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for Category.
 */
public interface CategoryJpaRepository extends JpaRepository<Category, Integer>, CategoryDao {
    /**
     * Find all order by id list.
     *
     * @return the list
     */
    @Query("SELECT c FROM Category c ORDER BY c.id")
    List<Category> findAllOrderById();

    /**
     * Find all by category id order by id list.
     *
     * @param categoryId the category id
     * @return the list
     */
    @Query("SELECT p FROM Product p WHERE p.category_id = :categoryId ORDER BY p.id")
    List<Product> findAllByCategoryIdOrderById(@Param("categoryId") Integer categoryId);

    /**
     * Hibernate manages schema; this call forces repository initialization.
     */
    @Override
    default void CreateCategoryTable() {
        count();
    }

    @Override
    default List<Category> findAllCategories() {
        return findAllOrderById();
    }

    @Override
    default List<Product> findAllProductsByCategoryId(Long categoryId) {
        Integer id = Math.toIntExact(categoryId);
        if (!existsById(id)) {
            throw new CategoryNotFoundException("Category not found for id: " + categoryId);
        }
        return findAllByCategoryIdOrderById(id);
    }

    @Override
    default Category addCategory(Category category) {
        category.setId(null);
        return save(category);
    }

    @Override
    default Category deleteCategory(Long id) {
        Integer categoryId = Math.toIntExact(id);
        Category existingCategory = findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found for id: " + id));
        delete(existingCategory);
        return existingCategory;
    }

    @Override
    default Category updateCategory(Category category) {
        Category existingCategory = findById(category.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found for id: " + category.getId()));
        existingCategory.setName(category.getName());
        return save(existingCategory);
    }
}

