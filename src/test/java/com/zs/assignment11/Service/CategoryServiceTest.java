package com.zs.assignment11.Service;

import com.zs.assignment11.dao.CategoryDao;
import com.zs.assignment11.exception.CannotCreateCategoryTableException;
import com.zs.assignment11.exception.CannotGetAllCategoryException;
import com.zs.assignment11.exception.CannotGetAllProductByCategoryIdException;
import com.zs.assignment11.exception.CategoryAlreadyExistsException;
import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

import org.junit.jupiter.params.provider.Arguments;

/**
 * The type Category service test.
 */
public class CategoryServiceTest {
    private CategoryDao categoryDao;
    private CategoryService categoryService;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        categoryDao = mock(CategoryDao.class);
        categoryService = new CategoryService(categoryDao);
    }

    /**
     * Create category table delegates to dao.
     */
    @Test
    void createCategoryTableDelegatesToDao() {
        categoryService.CreateCategoryTable();

        verify(categoryDao,times(1)).CreateCategoryTable();
    }

    /**
     * Create category table wraps data access exception.
     */
    @Test
    void createCategoryTableWrapsDataAccessException() {
        doThrow(new DataAccessResourceFailureException("db down"))
                .when(categoryDao).CreateCategoryTable();

        CannotCreateCategoryTableException exception = assertThrows(
                CannotCreateCategoryTableException.class,
                () -> categoryService.CreateCategoryTable()
        );

        assertEquals("Failed to create category table.", exception.getMessage());
        assertEquals("db down", exception.getCause().getMessage());
        verify(categoryDao).CreateCategoryTable();
    }


    /**
     * Category lists stream.
     *
     * @return the stream
     */
    static Stream<List<Category>> categoryLists() {
        return Stream.of(
                List.of(new Category(1, "electronics"), new Category(2, "fashion")),
                List.of(new Category(3, "sports")),
                List.of()
        );
    }

    /**
     * Gets all categories returns dao data.
     *
     * @param categories the categories
     */
    @ParameterizedTest
    @MethodSource("categoryLists")
    void getAllCategories_ReturnsDaoData(List<Category> categories) {
        when(categoryDao.findAllCategories()).thenReturn(categories);

        List<Category> result = categoryService.getAllCategories();

        assertSame(categories, result);
        verify(categoryDao).findAllCategories();
    }

    /**
     * Gets all categories wraps data retrieval failure exception.
     */
    @Test
    void getAllCategoriesWrapsDataRetrievalFailureException() {
        when(categoryDao.findAllCategories())
                .thenThrow(new DataRetrievalFailureException("read failed"));

        CannotGetAllCategoryException exception = assertThrows(
                CannotGetAllCategoryException.class,
                () -> categoryService.getAllCategories()
        );

        assertEquals("Failed to fetch all categories.", exception.getMessage());
        assertEquals("read failed", exception.getCause().getMessage());
        verify(categoryDao).findAllCategories();
    }

    /**
     * Valid category payloads stream.
     *
     * @return the stream
     */
    static Stream<Category> validCategories() {
        return Stream.of(
                new Category(1, "electronics"),
                new Category(2, "fashion")
        );
    }

    /**
     * Add category valid payload delegates to dao.
     *
     * @param category the category
     */
    @ParameterizedTest
    @MethodSource("validCategories")
    void addCategory_ValidPayload_DelegatesToDao(Category category) {
		Category savedCategory = new Category(10, category.getName());
		when(categoryDao.addCategory(category)).thenReturn(savedCategory);

		Category result = categoryService.addCategory(category);

		assertSame(savedCategory, result);
        verify(categoryDao).addCategory(category);
    }

    /**
     * Invalid categories stream.
     *
     * @return the stream
     */
    static Stream<Arguments> invalidCategories() {
        return Stream.of(
                arguments((Object) null, "Category payload is required."),
                arguments(new Category(1, null), "Category name must not be blank."),
                arguments(new Category(1, ""), "Category name must not be blank."),
                arguments(new Category(1, "   "), "Category name must not be blank.")
        );
    }

    /**
     * Add category invalid payload throws exception.
     *
     * @param category        the category
     * @param expectedMessage the expected message
     */
    @ParameterizedTest
    @MethodSource("invalidCategories")
    void addCategory_InvalidPayload_ThrowsException(Category category, String expectedMessage) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.addCategory(category)
        );

        assertEquals(expectedMessage, exception.getMessage());
        verifyNoInteractions(categoryDao);
    }

    /**
     * Add category duplicate name throws domain exception.
     */
    @Test
    void addCategory_DuplicateName_ThrowsCategoryAlreadyExistsException() {
        Category category = new Category(1, "electronics");
        doThrow(new org.springframework.dao.DuplicateKeyException("duplicate key"))
                .when(categoryDao).addCategory(category);

        CategoryAlreadyExistsException exception = assertThrows(
                CategoryAlreadyExistsException.class,
                () -> categoryService.addCategory(category)
        );

        assertEquals("Category already exists", exception.getMessage());
        verify(categoryDao).addCategory(category);
    }

    /**
     * Add category data access error throws runtime exception.
     */
    @Test
    void addCategory_DataAccessError_ThrowsRuntimeException() {
        Category category = new Category(1, "electronics");
        doThrow(new DataAccessResourceFailureException("insert failed"))
                .when(categoryDao).addCategory(category);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.addCategory(category)
        );

        assertEquals("Failed to add category to database.", exception.getMessage());
        assertEquals("insert failed", exception.getCause().getMessage());
        verify(categoryDao).addCategory(category);
    }


    /**
     * Valid category ids with products stream.
     *
     * @return the stream
     */
    static Stream<Arguments> validCategoryIdsWithProducts() {
        return Stream.of(
                arguments(1L, List.of(new Product(1, "Phone", 999.99, 1))),
                arguments(2L, List.of(new Product(4, "FaceCream", 4.5, 2), new Product(5, "faceGel", 10.0, 2))),
                arguments(3L, List.of())
        );
    }

    /**
     * Gets products by category id valid id returns dao data.
     *
     * @param categoryId the category id
     * @param products   the products
     */
    @ParameterizedTest
    @MethodSource("validCategoryIdsWithProducts")
    void getProductsByCategoryId_ValidId_ReturnsDaoData(Long categoryId, List<Product> products) {
        when(categoryDao.findAllProductsByCategoryId(categoryId)).thenReturn(products);

        List<Product> result = categoryService.getProductsByCategoryId(categoryId);

        assertSame(products, result);
        verify(categoryDao).findAllProductsByCategoryId(categoryId);
    }

    /**
     * Invalid category ids stream.
     *
     * @return the stream
     */
    static Stream<Arguments> invalidCategoryIds() {
        return Stream.of(
                arguments((Object) null),
                arguments(0L),
                arguments(-1L)
        );
    }

    /**
     * Gets products by category id invalid id throws exception.
     *
     * @param categoryId the category id
     */
    @ParameterizedTest
    @MethodSource("invalidCategoryIds")
    void getProductsByCategoryId_InvalidId_ThrowsException(Long categoryId) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.getProductsByCategoryId(categoryId)
        );

        assertEquals("Category id must be a positive number.", exception.getMessage());
        verifyNoInteractions(categoryDao);
    }

    /**
     * Gets products by category id wraps data access exception.
     */
    @Test
    void getProductsByCategoryIdWrapsDataAccessException() {
        when(categoryDao.findAllProductsByCategoryId(1L))
                .thenThrow(new DataAccessResourceFailureException("query failed"));

        CannotGetAllProductByCategoryIdException exception = assertThrows(
                CannotGetAllProductByCategoryIdException.class,
                () -> categoryService.getProductsByCategoryId(1L)
        );

        assertEquals("Failed to fetch all products for category id.", exception.getMessage());
        assertEquals("query failed", exception.getCause().getMessage());
        verify(categoryDao).findAllProductsByCategoryId(1L);
    }

    /**
     * Valid category ids stream.
     *
     * @return the stream
     */
    static Stream<Long> validDeleteCategoryIds() {
        return Stream.of(1L, 2L);
    }

    /**
     * Delete category valid id delegates to dao.
     *
     * @param categoryId the category id
     */
    @ParameterizedTest
    @MethodSource("validDeleteCategoryIds")
    void deleteCategory_ValidId_DelegatesToDao(Long categoryId) {
		Category deletedCategory = new Category(categoryId.intValue(), "electronics");
		when(categoryDao.deleteCategory(categoryId)).thenReturn(deletedCategory);

		Category result = categoryService.deleteCategory(categoryId);

		assertSame(deletedCategory, result);
        verify(categoryDao).deleteCategory(categoryId);
    }

    /**
     * Delete category invalid id throws exception.
     *
     * @param categoryId the category id
     */
    @ParameterizedTest
    @MethodSource("invalidCategoryIds")
    void deleteCategory_InvalidId_ThrowsException(Long categoryId) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> categoryService.deleteCategory(categoryId)
        );

        assertEquals("Category id must be a positive number.", exception.getMessage());
        verifyNoInteractions(categoryDao);
    }

    /**
     * Delete category data access error throws runtime exception.
     */
    @Test
    void deleteCategory_DataAccessError_ThrowsRuntimeException() {
        doThrow(new DataAccessResourceFailureException("delete failed"))
                .when(categoryDao).deleteCategory(1L);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> categoryService.deleteCategory(1L)
        );

        assertEquals("Failed to delete category from database.", exception.getMessage());
        assertEquals("delete failed", exception.getCause().getMessage());
        verify(categoryDao).deleteCategory(1L);
    }
}
