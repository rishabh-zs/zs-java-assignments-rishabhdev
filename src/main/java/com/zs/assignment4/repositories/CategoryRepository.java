package com.zs.assignment4.repositories;

import com.zs.assignment4.models.Category;
import java.util.List;

/**
 * The interface Category repository.
 */
public interface CategoryRepository {
    /**
     * Save.
     *
     * @param category the category
     */
    void save(Category category);

    /**
     * Find by name category.
     *
     * @param name the name
     * @return the category
     */
    Category findByName(String name);

    /**
     * Delete by name.
     *
     * @param name the name
     * @return the deleted category
     */
    Category deleteByName(String name);

    /**
     * Find all list.
     *
     * @return the list
     */
    List<Category> findAll();

    /**
     * Exists by name boolean.
     *
     * @param name the name
     * @return the boolean
     */
    boolean existsByName(String name);
}
