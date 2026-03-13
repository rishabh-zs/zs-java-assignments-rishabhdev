package com.zs.assignment11.Service;

import com.zs.assignment11.dao.ProductDao;
import com.zs.assignment11.exception.CannotCreateProductTableException;
import com.zs.assignment11.exception.ProductAlreadyExistsException;
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
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.params.provider.Arguments;

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

	/**
	 * Valid products stream.
	 *
	 * @return the stream
	 */
	static Stream<Product> validProducts() {
		return Stream.of(
				new Product(1, "Phone", 999.99, 1),
				new Product(2, "FaceCream", 4.50, 2)
		);
	}

	/**
	 * Add product valid payload delegates to dao.
	 *
	 * @param product the product
	 */
	@ParameterizedTest
	@MethodSource("validProducts")
	void addProduct_ValidPayload_DelegatesToDao(Product product) {
		Product savedProduct = new Product(10, product.getName(), product.getPrice(), product.getCategoryId());
		when(productDao.addProduct(product)).thenReturn(savedProduct);

		Product result = productService.addProduct(product);

		assertSame(savedProduct, result);
		verify(productDao).addProduct(product);
	}

	/**
	 * Invalid products stream.
	 *
	 * @return the stream
	 */
	static Stream<Arguments> invalidProducts() {
		return Stream.of(
				arguments((Object) null, "Product payload is required."),
				arguments(new Product(1, null, 1.0, 1), "Product name must not be blank."),
				arguments(new Product(1, "", 1.0, 1), "Product name must not be blank."),
				arguments(new Product(1, "   ", 1.0, 1), "Product name must not be blank."),
				arguments(new Product(1, "Phone", null, 1), "Product price must be zero or greater."),
				arguments(new Product(1, "Phone", -1.0, 1), "Product price must be zero or greater."),
				arguments(new Product(1, "Phone", 1.0, null), "Category id must be a positive number."),
				arguments(new Product(1, "Phone", 1.0, 0), "Category id must be a positive number."),
				arguments(new Product(1, "Phone", 1.0, -1), "Category id must be a positive number.")
		);
	}

	/**
	 * Add product invalid payload throws exception.
	 *
	 * @param product          the product
	 * @param expectedMessage  the expected message
	 */
	@ParameterizedTest
	@MethodSource("invalidProducts")
	void addProduct_InvalidPayload_ThrowsException(Product product, String expectedMessage) {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> productService.addProduct(product)
		);

		assertEquals(expectedMessage, exception.getMessage());
		verifyNoInteractions(productDao);
	}

	/**
	 * Add product duplicate name throws domain exception.
	 */
	@Test
	void addProduct_DuplicateName_ThrowsProductAlreadyExistsException() {
		Product product = new Product(1, "Phone", 999.99, 1);
		doThrow(new org.springframework.dao.DuplicateKeyException("duplicate key"))
				.when(productDao).addProduct(product);

		ProductAlreadyExistsException exception = assertThrows(
				ProductAlreadyExistsException.class,
				() -> productService.addProduct(product)
		);

		assertEquals("Product already exists: Phone", exception.getMessage());
		verify(productDao).addProduct(product);
	}

	/**
	 * Add product data access error throws runtime exception.
	 */
	@Test
	void addProduct_DataAccessError_ThrowsRuntimeException() {
		Product product = new Product(1, "Phone", 999.99, 1);
		when(productDao.addProduct(product))
				.thenThrow(new DataAccessResourceFailureException("insert failed"));

		RuntimeException exception = assertThrows(
				RuntimeException.class,
				() -> productService.addProduct(product)
		);

		assertEquals("Failed to add product to database.", exception.getMessage());
		assertEquals("insert failed", exception.getCause().getMessage());
		verify(productDao).addProduct(product);
	}

	/**
	 * Valid delete product ids stream.
	 *
	 * @return the stream
	 */
	static Stream<Long> validDeleteProductIds() {
		return Stream.of(1L, 2L);
	}

	/**
	 * Delete product valid id delegates to dao.
	 *
	 * @param productId the product id
	 */
	@ParameterizedTest
	@MethodSource("validDeleteProductIds")
	void deleteProduct_ValidId_DelegatesToDao(Long productId) {
		Product deletedProduct = new Product(productId.intValue(), "Phone", 999.99, 1);
		when(productDao.deleteProduct(productId)).thenReturn(deletedProduct);

		Product result = productService.deleteProduct(productId);

		assertSame(deletedProduct, result);
		verify(productDao).deleteProduct(productId);
	}

	/**
	 * Invalid delete product ids stream.
	 *
	 * @return the stream
	 */
	static Stream<Arguments> invalidDeleteProductIds() {
		return Stream.of(
				arguments((Object) null),
				arguments(0L),
				arguments(-1L)
		);
	}

	/**
	 * Delete product invalid id throws exception.
	 *
	 * @param productId the product id
	 */
	@ParameterizedTest
	@MethodSource("invalidDeleteProductIds")
	void deleteProduct_InvalidId_ThrowsException(Long productId) {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> productService.deleteProduct(productId)
		);

		assertEquals("Product id must be a positive number.", exception.getMessage());
		verifyNoInteractions(productDao);
	}

	/**
	 * Delete product data access error throws runtime exception.
	 */
	@Test
	void deleteProduct_DataAccessError_ThrowsRuntimeException() {
		when(productDao.deleteProduct(1L))
				.thenThrow(new DataAccessResourceFailureException("delete failed"));

		RuntimeException exception = assertThrows(
				RuntimeException.class,
				() -> productService.deleteProduct(1L)
		);

		assertEquals("Failed to delete Product from database.", exception.getMessage());
		assertEquals("delete failed", exception.getCause().getMessage());
		verify(productDao).deleteProduct(1L);
	}

	@Test
	void updateProduct_ValidPayload_DelegatesToDao() {
		Product updateRequest = new Product(1, "Phone Pro", 1099.99, null);
		Product updatedProduct = new Product(1, "Phone Pro", 1099.99, 1);
		when(productDao.updateProduct(updateRequest)).thenReturn(updatedProduct);

		Product result = productService.updateProduct(updateRequest);

		assertSame(updatedProduct, result);
		verify(productDao).updateProduct(updateRequest);
	}

	static Stream<Arguments> invalidUpdateProducts() {
		return Stream.of(
				arguments((Object) null, "Product payload is required."),
				arguments(new Product(null, "Phone", 1.0, 1), "Product id must be a positive number."),
				arguments(new Product(0, "Phone", 1.0, 1), "Product id must be a positive number."),
				arguments(new Product(-1, "Phone", 1.0, 1), "Product id must be a positive number."),
				arguments(new Product(1, null, 1.0, 1), "Product name must not be blank."),
				arguments(new Product(1, "", 1.0, 1), "Product name must not be blank."),
				arguments(new Product(1, "   ", 1.0, 1), "Product name must not be blank."),
				arguments(new Product(1, "Phone", null, 1), "Product price must be zero or greater."),
				arguments(new Product(1, "Phone", -1.0, 1), "Product price must be zero or greater.")
		);
	}

	@ParameterizedTest
	@MethodSource("invalidUpdateProducts")
	void updateProduct_InvalidPayload_ThrowsException(Product product, String expectedMessage) {
		IllegalArgumentException exception = assertThrows(
				IllegalArgumentException.class,
				() -> productService.updateProduct(product)
		);

		assertEquals(expectedMessage, exception.getMessage());
		verifyNoInteractions(productDao);
	}

	@Test
	void updateProduct_DataAccessError_ThrowsRuntimeException() {
		Product updateRequest = new Product(1, "Phone Pro", 1099.99, 1);
		when(productDao.updateProduct(updateRequest))
				.thenThrow(new DataAccessResourceFailureException("update failed"));

		RuntimeException exception = assertThrows(
				RuntimeException.class,
				() -> productService.updateProduct(updateRequest)
		);

		assertEquals("Failed to update product in database.", exception.getMessage());
		assertEquals("update failed", exception.getCause().getMessage());
		verify(productDao).updateProduct(updateRequest);
	}
}
