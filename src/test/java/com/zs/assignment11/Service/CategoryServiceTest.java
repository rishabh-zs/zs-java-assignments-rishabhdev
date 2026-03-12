package com.zs.assignment11.Service;

import com.zs.assignment11.dao.CategoryDao;
import com.zs.assignment11.exception.CannotCreateCategoryTableException;
import com.zs.assignment11.exception.CannotGetAllCategoryException;
import com.zs.assignment11.exception.CannotGetAllProductByCategoryIdException;
import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataRetrievalFailureException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {
	private CategoryDao categoryDao;
	private CategoryService categoryService;

	@BeforeEach
	void setUp() {
		categoryDao = mock(CategoryDao.class);
		categoryService = new CategoryService(categoryDao);  // real service, mocked DAO
	}

	@Test
	void createCategoryTableDelegatesToDao() {
		categoryService.CreateCategoryTable();

		verify(categoryDao).CreateCategoryTable();
	}

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

	@Test
	void getAllCategoriesReturnsDaoData() {
		List<Category> categories = List.of(
				new Category(1, "electronics"),
				new Category(2, "fashion")
		);
		when(categoryDao.findAllCategories()).thenReturn(categories);

		List<Category> result = categoryService.getAllCategories();

		assertSame(categories, result);
		verify(categoryDao).findAllCategories();
	}

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

	@Test
	void getProductsByCategoryIdReturnsDaoData() {
		List<Product> products = List.of(
				new Product(4, "FaceCream", 4.5, 2),
				new Product(5, "faceGel", 10.0, 2)
		);
		when(categoryDao.findAllProductsByCategoryId(2L)).thenReturn(products);

		List<Product> result = categoryService.getProductsByCategoryId(2L);

		assertSame(products, result);
		verify(categoryDao).findAllProductsByCategoryId(2L);
	}

	@Test
	void getProductsByCategoryIdThrowsWhenIdIsNull() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> categoryService.getProductsByCategoryId(null)
		);

		assertEquals("Category id must be a positive number.", exception.getMessage());
		verifyNoInteractions(categoryDao);
	}

	@Test
	void getProductsByCategoryIdThrowsWhenIdIsZero() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> categoryService.getProductsByCategoryId(0L)
		);

		assertEquals("Category id must be a positive number.", exception.getMessage());
		verifyNoInteractions(categoryDao);
	}

	@Test
	void getProductsByCategoryIdThrowsWhenIdIsNegative() {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> categoryService.getProductsByCategoryId(-1L)
		);

		assertEquals("Category id must be a positive number.", exception.getMessage());
		verifyNoInteractions(categoryDao);
	}

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
}
