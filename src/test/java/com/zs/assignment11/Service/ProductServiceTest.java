package com.zs.assignment11.Service;

import com.zs.assignment11.dao.ProductDao;
import com.zs.assignment11.exception.CannotCreateProductTableException;
import com.zs.assignment11.model.Product;
import com.zs.assignment11.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Product service test.
 */
public class ProductServiceTest {
	private ProductDao productDao;
	private ProductService productService;

	/**
	 * Sets up.
	 */
	@BeforeEach
	void setUp() {
		productDao = mock(ProductDao.class);
		productService = new ProductService(productDao);
	}

	/**
	 * Create product table delegates to dao.
	 */
	@Test
	void createProductTableDelegatesToDao() {
		productService.CreateProductTable();

		verify(productDao, times(1)).CreateProductTable();
	}

	/**
	 * Create product table wraps data access exception.
	 */
	@Test
	void createProductTableWrapsDataAccessException() {
		doThrow(new DataAccessResourceFailureException("db down"))
				.when(productDao).CreateProductTable();

		CannotCreateProductTableException exception = assertThrows(
				CannotCreateProductTableException.class,
				() -> productService.CreateProductTable()
		);

		assertEquals("Failed to Create Product table.", exception.getMessage());
		assertEquals("db down", exception.getCause().getMessage());
		verify(productDao, times(1)).CreateProductTable();
	}

	/**
	 * Product lists stream.
	 *
	 * @return the stream
	 */
	static Stream<List<Product>> productLists() {
		return Stream.of(
				List.of(
						new Product(1, "Phone", 999.99, 1),
						new Product(2, "Laptop", 1499.00, 1)
				),
				List.of(new Product(3, "FaceCream", 4.5, 2)),
				List.of()
		);
	}

	/**
	 * Gets all products returns dao data.
	 *
	 * @param products the products
	 */
	@ParameterizedTest
	@MethodSource("productLists")
	void getAllProducts_ReturnsDaoData(List<Product> products) {
		when(productDao.findAllProducts()).thenReturn(products);

		List<Product> result = productService.getAllProducts();

		assertSame(products, result);
		verify(productDao, times(1)).findAllProducts();
	}

	/**
	 * Gets all products wraps data access exception.
	 */
	@Test
	void getAllProductsWrapsDataAccessException() {
		when(productDao.findAllProducts())
				.thenThrow(new DataAccessResourceFailureException("read failed"));

		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> productService.getAllProducts()
		);

		assertEquals("Failed to fetch all products", exception.getMessage());
		assertEquals("read failed", exception.getCause().getMessage());
		verify(productDao, times(1)).findAllProducts();
	}
}
