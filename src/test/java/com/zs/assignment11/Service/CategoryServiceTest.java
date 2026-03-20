package com.zs.assignment11.Service;

import com.zs.assignment11.dao.CategoryJpaRepository;
import com.zs.assignment11.exception.CannotGetAllCategoryException;
import com.zs.assignment11.exception.CannotGetAllProductByCategoryIdException;
import com.zs.assignment11.exception.CategoryAlreadyExistsException;
import com.zs.assignment11.exception.CategoryNotFoundException;
import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class CategoryServiceTest {
    private CategoryJpaRepository categoryJpaRepository;
    private CategoryService categoryService;

    static Stream<List<Category>> categoryLists() {
        return Stream.of(
                List.of(new Category(1, "electronics"), new Category(2, "fashion")),
                List.of(new Category(3, "sports")),
                List.of()
        );
    }

    static Stream<Arguments> invalidCategories() {
        return Stream.of(
                arguments(null, "Category payload is required."),
                arguments(new Category(1, null), "Category name must not be blank."),
                arguments(new Category(1, ""), "Category name must not be blank."),
                arguments(new Category(1, "   "), "Category name must not be blank.")
        );
    }

    static Stream<Arguments> validCategoryIdsWithProducts() {
        return Stream.of(
                arguments(1, List.of(new Product(1, "Phone", 999.99, 1))),
                arguments(2, List.of(new Product(4, "FaceCream", 4.5, 2), new Product(5, "faceGel", 10.0, 2))),
                arguments(3, List.of())
        );
    }

    static Stream<Arguments> invalidCategoryIds() {
        return Stream.of(
                arguments((Object) null),
                arguments(0),
                arguments(-1)
        );
    }

    static Stream<Arguments> invalidUpdateCategories() {
        return Stream.of(
                arguments(null, "Category payload is required."),
                arguments(new Category(null, "electronics"), "Category id must be a positive number."),
                arguments(new Category(0, "electronics"), "Category id must be a positive number."),
                arguments(new Category(-1, "electronics"), "Category id must be a positive number."),
                arguments(new Category(1, null), "Category name must not be blank."),
                arguments(new Category(1, ""), "Category name must not be blank."),
                arguments(new Category(1, "   "), "Category name must not be blank.")
        );
    }

    @BeforeEach
    void setUp() {
        categoryJpaRepository = mock(CategoryJpaRepository.class);
        categoryService = new CategoryService(categoryJpaRepository);
    }

    @ParameterizedTest
    @MethodSource("categoryLists")
    void getAllCategories_ReturnsRepositoryData(List<Category> categories) {
        when(categoryJpaRepository.findAllByOrderById()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertSame(categories, result);
        verify(categoryJpaRepository).findAllByOrderById();
    }

    @Test
    void getAllCategories_WrapsDataRetrievalFailureException() {
        when(categoryJpaRepository.findAllByOrderById())
                .thenThrow(new DataRetrievalFailureException("read failed"));

        CannotGetAllCategoryException exception = assertThrows(
                CannotGetAllCategoryException.class,
                () -> categoryService.getAllCategories()
        );

        assertEquals("Failed to fetch all categories.", exception.getMessage());
        assertEquals("read failed", exception.getCause().getMessage());
        verify(categoryJpaRepository).findAllByOrderById();
    }

    @Test
    void addCategory_ValidPayload_DelegatesToRepositorySave() {
        Category category = new Category(99, "electronics");
        Category saved = new Category(1, "electronics");
        when(categoryJpaRepository.save(any(Category.class))).thenReturn(saved);

        Category result = categoryService.addCategory(category);

        assertSame(saved, result);
        assertNull(category.getId());
        verify(categoryJpaRepository).save(category);
    }

    @ParameterizedTest
    @MethodSource("invalidCategories")
    void addCategory_InvalidPayload_ThrowsException(Category category, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.addCategory(category)
        );

        assertEquals(expectedMessage, exception.getMessage());
        verifyNoInteractions(categoryJpaRepository);
    }

    @Test
    void addCategory_DuplicateName_ThrowsCategoryAlreadyExistsException() {
        Category category = new Category(1, "electronics");
        doThrow(new DataIntegrityViolationException("duplicate"))
                .when(categoryJpaRepository).save(category);

        CategoryAlreadyExistsException exception = assertThrows(
                CategoryAlreadyExistsException.class,
                () -> categoryService.addCategory(category)
        );

        assertEquals("Category already exists", exception.getMessage());
        verify(categoryJpaRepository).save(category);
    }

    @ParameterizedTest
    @MethodSource("validCategoryIdsWithProducts")
    void getProductsByCategoryId_ValidId_ReturnsRepositoryData(Integer categoryId, List<Product> products) {
        when(categoryJpaRepository.existsById(categoryId)).thenReturn(true);
        when(categoryJpaRepository.findAllProductsByCategoryIdOrderById(categoryId)).thenReturn(products);

        List<Product> result = categoryService.getProductsByCategoryId(categoryId);

        assertSame(products, result);
        verify(categoryJpaRepository).existsById(categoryId);
        verify(categoryJpaRepository).findAllProductsByCategoryIdOrderById(categoryId);
    }

    @ParameterizedTest
    @MethodSource("invalidCategoryIds")
    void getProductsByCategoryId_InvalidId_ThrowsException(Integer categoryId) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.getProductsByCategoryId(categoryId)
        );

        assertEquals("Category id must be a positive number.", exception.getMessage());
        verifyNoInteractions(categoryJpaRepository);
    }

    @Test
    void getProductsByCategoryId_ThrowsCategoryNotFoundWhenIdMissing() {
        when(categoryJpaRepository.existsById(1)).thenReturn(false);

        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.getProductsByCategoryId(1)
        );

        assertEquals("Category not found for id: 1", exception.getMessage());
        verify(categoryJpaRepository).existsById(1);
    }

    @Test
    void getProductsByCategoryId_WrapsDataAccessException() {
        when(categoryJpaRepository.existsById(1)).thenReturn(true);
        when(categoryJpaRepository.findAllProductsByCategoryIdOrderById(1))
                .thenThrow(new DataAccessResourceFailureException("query failed"));

        CannotGetAllProductByCategoryIdException exception = assertThrows(
                CannotGetAllProductByCategoryIdException.class,
                () -> categoryService.getProductsByCategoryId(1)
        );

        assertEquals("Failed to fetch all products for category id.", exception.getMessage());
        assertEquals("query failed", exception.getCause().getMessage());
    }

    @Test
    void deleteCategory_ValidId_DeletesAndReturnsCategory() {
        Category category = new Category(1, "electronics");
        when(categoryJpaRepository.findById(1)).thenReturn(Optional.of(category));

        Category result = categoryService.deleteCategory(1);

        assertSame(category, result);
        verify(categoryJpaRepository).findById(1);
        verify(categoryJpaRepository).delete(category);
    }

    @Test
    void deleteCategory_NotFound_ThrowsCategoryNotFoundException() {
        when(categoryJpaRepository.findById(1)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(1)
        );

        assertEquals("Category not found for id: 1", exception.getMessage());
        verify(categoryJpaRepository).findById(1);
    }

    @Test
    void deleteCategory_DataAccessError_ThrowsRuntimeException() {
        Category category = new Category(1, "electronics");
        when(categoryJpaRepository.findById(1)).thenReturn(Optional.of(category));
        doThrow(new DataAccessResourceFailureException("delete failed"))
                .when(categoryJpaRepository).delete(category);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.deleteCategory(1)
        );

        assertEquals("Failed to delete category from database.", exception.getMessage());
        assertEquals("delete failed", exception.getCause().getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateCategories")
    void updateCategory_InvalidPayload_ThrowsException(Category category, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.updateCategory(category)
        );

        assertEquals(expectedMessage, exception.getMessage());
        verifyNoInteractions(categoryJpaRepository);
    }

    @Test
    void updateCategory_NotFound_ThrowsCategoryNotFoundException() {
        Category request = new Category(1, "updated");
        when(categoryJpaRepository.findById(1)).thenReturn(Optional.empty());

        CategoryNotFoundException exception = assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.updateCategory(request)
        );

        assertEquals("Category not found for id: 1", exception.getMessage());
    }

    @Test
    void updateCategory_ValidPayload_UpdatesAndReturnsSavedCategory() {
        Category request = new Category(1, "updated");
        Category existing = new Category(1, "old");
        Category saved = new Category(1, "updated");
        when(categoryJpaRepository.findById(1)).thenReturn(Optional.of(existing));
        when(categoryJpaRepository.save(existing)).thenReturn(saved);

        Category result = categoryService.updateCategory(request);

        assertSame(saved, result);
        assertEquals("updated", existing.getName());
        verify(categoryJpaRepository).findById(1);
        verify(categoryJpaRepository).save(existing);
    }

    @Test
    void updateCategory_DataAccessError_ThrowsRuntimeException() {
        Category request = new Category(1, "updated");
        Category existing = new Category(1, "old");
        when(categoryJpaRepository.findById(1)).thenReturn(Optional.of(existing));
        when(categoryJpaRepository.save(existing))
                .thenThrow(new DataAccessResourceFailureException("update failed"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.updateCategory(request)
        );

        assertEquals("Failed to update category in database.", exception.getMessage());
        assertEquals("update failed", exception.getCause().getMessage());
    }
}
