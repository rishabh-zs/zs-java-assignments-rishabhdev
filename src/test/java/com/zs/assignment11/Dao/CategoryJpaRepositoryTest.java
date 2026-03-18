package com.zs.assignment11.Dao;

import com.zs.assignment11.dao.CategoryJpaRepository;
import com.zs.assignment11.exception.CategoryNotFoundException;
import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Category jpa repository test.
 */
class CategoryJpaRepositoryTest {

    private CategoryJpaRepository categoryJpaRepository;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        categoryJpaRepository = mock(CategoryJpaRepository.class, Answers.CALLS_REAL_METHODS);
    }

    /**
     * Create category table delegates to count.
     */
    @Test
    void createCategoryTable_DelegatesToCount() {
        when(categoryJpaRepository.count()).thenReturn(1L);

        categoryJpaRepository.CreateCategoryTable();

        verify(categoryJpaRepository).count();
    }

    /**
     * Find all categories returns ordered categories.
     */
    @Test
    void findAllCategories_ReturnsOrderedCategories() {
        List<Category> categories = List.of(new Category(1, "electronics"), new Category(2, "fashion"));
        when(categoryJpaRepository.findAllOrderById()).thenReturn(categories);

        List<Category> result = categoryJpaRepository.findAllCategories();

        assertEquals(2, result.size());
        assertEquals("electronics", result.getFirst().getName());
        verify(categoryJpaRepository).findAllOrderById();
    }

    /**
     * Find all products by category id returns products when category exists.
     */
    @Test
    void findAllProductsByCategoryId_ReturnsProductsWhenCategoryExists() {
        List<Product> products = List.of(
                new Product(1, "laptop", 1000.0, 1),
                new Product(2, "tv", 2000.0, 1)
        );
        when(categoryJpaRepository.existsById(1)).thenReturn(true);
        when(categoryJpaRepository.findAllByCategoryIdOrderById(1)).thenReturn(products);

        List<Product> result = categoryJpaRepository.findAllProductsByCategoryId(1L);

        assertEquals(2, result.size());
        assertEquals("laptop", result.getFirst().getName());
        verify(categoryJpaRepository).existsById(1);
        verify(categoryJpaRepository).findAllByCategoryIdOrderById(1);
    }

    /**
     * Find all products by category id throws when category missing.
     */
    @Test
    void findAllProductsByCategoryId_ThrowsWhenCategoryMissing() {
        when(categoryJpaRepository.existsById(99)).thenReturn(false);

        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryJpaRepository.findAllProductsByCategoryId(99L)
        );

        assertEquals("Category not found for id: 99", exception.getMessage());
        verify(categoryJpaRepository).existsById(99);
        verify(categoryJpaRepository, never()).findAllByCategoryIdOrderById(any());
    }

    /**
     * Add category sets null id and saves.
     */
    @Test
    void addCategory_SetsNullIdAndSaves() {
        Category request = new Category(10, "electronics");
        Category saved = new Category(1, "electronics");
        when(categoryJpaRepository.save(any(Category.class))).thenReturn(saved);

        Category result = categoryJpaRepository.addCategory(request);

        assertNull(request.getId());
        assertSame(saved, result);
        verify(categoryJpaRepository).save(request);
    }

    /**
     * Delete category deletes and returns entity when found.
     */
    @Test
    void deleteCategory_DeletesAndReturnsEntityWhenFound() {
        Category existing = new Category(1, "electronics");
        when(categoryJpaRepository.findById(1)).thenReturn(Optional.of(existing));

        Category result = categoryJpaRepository.deleteCategory(1L);

        assertSame(existing, result);
        verify(categoryJpaRepository).findById(1);
        verify(categoryJpaRepository).delete(existing);
    }

    /**
     * Delete category throws when not found.
     */
    @Test
    void deleteCategory_ThrowsWhenNotFound() {
        when(categoryJpaRepository.findById(7)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryJpaRepository.deleteCategory(7L)
        );

        assertEquals("Category not found for id: 7", exception.getMessage());
        verify(categoryJpaRepository).findById(7);
        verify(categoryJpaRepository, never()).delete(any(Category.class));
    }

    /**
     * Update category updates name and saves.
     */
    @Test
    void updateCategory_UpdatesNameAndSaves() {
        Category existing = new Category(2, "old");
        Category request = new Category(2, "updated");
        when(categoryJpaRepository.findById(2)).thenReturn(Optional.of(existing));
        when(categoryJpaRepository.save(existing)).thenReturn(existing);

        Category result = categoryJpaRepository.updateCategory(request);

        assertEquals("updated", existing.getName());
        assertSame(existing, result);
        verify(categoryJpaRepository).findById(2);
        verify(categoryJpaRepository).save(existing);
    }

    /**
     * Update category throws when not found.
     */
    @Test
    void updateCategory_ThrowsWhenNotFound() {
        Category request = new Category(42, "updated");
        when(categoryJpaRepository.findById(42)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryJpaRepository.updateCategory(request)
        );

        assertEquals("Category not found for id: 42", exception.getMessage());
        verify(categoryJpaRepository).findById(42);
        verify(categoryJpaRepository, never()).save(any(Category.class));
    }
}

