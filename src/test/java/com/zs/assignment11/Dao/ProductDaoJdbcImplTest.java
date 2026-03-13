package com.zs.assignment11.Dao;

import com.zs.assignment11.dao.ProductDaoJdbcImpl;
import com.zs.assignment11.exception.ProductNotFoundException;
import com.zs.assignment11.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Product dao jdbc test.
 */
public class ProductDaoJdbcImplTest {
	private static final String CHECK_PRODUCT_TABLE_EXISTS_SQL =
			"SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'product')";
	private static final String CREATE_PRODUCT_TABLE_SQL = "CREATE TABLE IF NOT EXISTS product (" +
			"id SERIAL PRIMARY KEY, " +
			"name VARCHAR(255) NOT NULL UNIQUE, " +
			"price DOUBLE PRECISION NOT NULL CHECK (price >= 0), " +
			"category_id INTEGER NOT NULL, " +
			"CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE" +
			")";
	private static final String FIND_ALL_PRODUCTS_SQL = "SELECT id, name, price, category_id FROM product ORDER BY id";
	private static final String INSERT_PRODUCT_SQL = "INSERT INTO product (name, price, category_id) VALUES (?, ?, ?) RETURNING id";
	private static final String FIND_PRODUCT_BY_ID_SQL = "SELECT id, name, price, category_id FROM product WHERE id = ?";
	private static final String DELETE_PRODUCT_SQL = "DELETE FROM product WHERE id = ?";
	private static final String UPDATE_PRODUCT_SQL = "UPDATE product SET name = ?, price = ? WHERE id = ?";

	private JdbcTemplate jdbcTemplate;
	private ProductDaoJdbcImpl productDaoJdbc;

	/**
	 * Sets up.
	 */
	@BeforeEach
	void setUp() {
		jdbcTemplate = mock(JdbcTemplate.class);
		productDaoJdbc = new ProductDaoJdbcImpl(jdbcTemplate);
	}

	/**
	 * Create product table skips create when table exists.
	 */
	@Test
	void createProductTable_SkipsCreateWhenTableExists() {
		when(jdbcTemplate.queryForObject(CHECK_PRODUCT_TABLE_EXISTS_SQL, Boolean.class)).thenReturn(true);

		productDaoJdbc.CreateProductTable();

		verify(jdbcTemplate, times(1)).queryForObject(CHECK_PRODUCT_TABLE_EXISTS_SQL, Boolean.class);
		verify(jdbcTemplate, never()).execute(CREATE_PRODUCT_TABLE_SQL);
	}

	/**
	 * Create product table creates table when missing.
	 */
	@Test
	void createProductTable_CreatesTableWhenMissing() {
		when(jdbcTemplate.queryForObject(CHECK_PRODUCT_TABLE_EXISTS_SQL, Boolean.class)).thenReturn(false);

		productDaoJdbc.CreateProductTable();

		verify(jdbcTemplate, times(1)).queryForObject(CHECK_PRODUCT_TABLE_EXISTS_SQL, Boolean.class);
		verify(jdbcTemplate, times(1)).execute(CREATE_PRODUCT_TABLE_SQL);
	}

	/**
	 * Create product table propagates data access exception.
	 */
	@Test
	void createProductTable_PropagatesDataAccessException() {
		when(jdbcTemplate.queryForObject(CHECK_PRODUCT_TABLE_EXISTS_SQL, Boolean.class))
				.thenThrow(new DataAccessResourceFailureException("db down"));

		DataAccessResourceFailureException exception = assertThrows(
				DataAccessResourceFailureException.class,
				() -> productDaoJdbc.CreateProductTable()
		);

		assertEquals("db down", exception.getMessage());
		verify(jdbcTemplate, times(1)).queryForObject(CHECK_PRODUCT_TABLE_EXISTS_SQL, Boolean.class);
	}

	/**
	 * Finds all products maps result set.
	 */
	@Test
	void findAllProducts_MapsResultSet() {
		when(jdbcTemplate.query(eq(FIND_ALL_PRODUCTS_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any()))
				.thenAnswer(invocation -> {
					RowMapper<Product> rowMapper = invocation.getArgument(1);

					ResultSet firstRow = mock(ResultSet.class);
					when(firstRow.getInt("id")).thenReturn(1);
					when(firstRow.getString("name")).thenReturn("Phone");
					when(firstRow.getDouble("price")).thenReturn(999.99);
					when(firstRow.getInt("category_id")).thenReturn(1);

					ResultSet secondRow = mock(ResultSet.class);
					when(secondRow.getInt("id")).thenReturn(2);
					when(secondRow.getString("name")).thenReturn("Laptop");
					when(secondRow.getDouble("price")).thenReturn(1499.00);
					when(secondRow.getInt("category_id")).thenReturn(1);

					return List.of(
							rowMapper.mapRow(firstRow, 0),
							rowMapper.mapRow(secondRow, 1)
					);
				});

		List<Product> result = productDaoJdbc.findAllProducts();

		assertEquals(2, result.size());
		assertEquals("Phone", result.getFirst().getName());
		assertEquals(999.99, result.getFirst().getPrice());
		assertEquals(1, result.getFirst().getCategoryId());
		assertEquals("Laptop", result.get(1).getName());
		assertEquals(1499.00, result.get(1).getPrice());
		assertEquals(1, result.get(1).getCategoryId());
		verify(jdbcTemplate, times(1)).query(eq(FIND_ALL_PRODUCTS_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any());
	}

	/**
	 * Finds all products propagates data access exception.
	 */
	@Test
	void findAllProducts_PropagatesDataAccessException() {
		when(jdbcTemplate.query(eq(FIND_ALL_PRODUCTS_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any()))
				.thenThrow(new DataAccessResourceFailureException("query failed"));

		DataAccessResourceFailureException exception = assertThrows(
				DataAccessResourceFailureException.class,
				() -> productDaoJdbc.findAllProducts()
		);

		assertEquals("query failed", exception.getMessage());
		verify(jdbcTemplate, times(1)).query(eq(FIND_ALL_PRODUCTS_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any());
	}

	/**
	 * Add product inserts row.
	 */
	@Test
	void addProduct_InsertsRow() {
		Product product = new Product(1, "Phone", 999.99, 1);
		when(jdbcTemplate.queryForObject(
				eq(INSERT_PRODUCT_SQL),
				eq(Integer.class),
				eq(product.getName()),
				eq(product.getPrice()),
				eq(product.getCategoryId())
		)).thenReturn(10);

		Product result = productDaoJdbc.addProduct(product);

		assertNotNull(result);
		assertEquals(10, result.getId());
		assertEquals("Phone", result.getName());
		assertEquals(999.99, result.getPrice());
		assertEquals(1, result.getCategoryId());
		verify(jdbcTemplate, times(1)).queryForObject(
				eq(INSERT_PRODUCT_SQL),
				eq(Integer.class),
				eq(product.getName()),
				eq(product.getPrice()),
				eq(product.getCategoryId())
		);
	}

	/**
	 * Add product throws when insert count is not one.
	 */
	@Test
	void addProduct_ThrowsWhenInsertCountIsNotOne() {
		Product product = new Product(1, "Phone", 999.99, 1);
		when(jdbcTemplate.queryForObject(
				eq(INSERT_PRODUCT_SQL),
				eq(Integer.class),
				eq(product.getName()),
				eq(product.getPrice()),
				eq(product.getCategoryId())
		)).thenReturn(null);

		IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> productDaoJdbc.addProduct(product)
		);

		assertEquals("Unable to insert product: Phone", exception.getMessage());
		verify(jdbcTemplate, times(1)).queryForObject(
				eq(INSERT_PRODUCT_SQL),
				eq(Integer.class),
				eq(product.getName()),
				eq(product.getPrice()),
				eq(product.getCategoryId())
		);
	}

	/**
	 * Delete product deletes row.
	 */
	@Test
	void deleteProduct_DeletesRow() {
		Product storedProduct = new Product(1, "Phone", 999.99, 1);
		when(jdbcTemplate.queryForObject(eq(FIND_PRODUCT_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(1L)))
				.thenReturn(storedProduct);
		when(jdbcTemplate.update(DELETE_PRODUCT_SQL, 1L)).thenReturn(1);

		Product result = productDaoJdbc.deleteProduct(1L);

		assertSame(storedProduct, result);
		verify(jdbcTemplate, times(1)).queryForObject(eq(FIND_PRODUCT_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(1L));
		verify(jdbcTemplate, times(1)).update(DELETE_PRODUCT_SQL, 1L);
	}

	/**
	 * Delete product throws when delete count is not one.
	 */
	@Test
	void deleteProduct_ThrowsWhenDeleteCountIsNotOne() {
		Product storedProduct = new Product(1, "Phone", 999.99, 1);
		when(jdbcTemplate.queryForObject(eq(FIND_PRODUCT_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(1L)))
				.thenReturn(storedProduct);
		when(jdbcTemplate.update(DELETE_PRODUCT_SQL, 1L)).thenReturn(0);

		IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> productDaoJdbc.deleteProduct(1L)
		);

		assertEquals("Unable to delete product: 1", exception.getMessage());
		verify(jdbcTemplate, times(1)).queryForObject(eq(FIND_PRODUCT_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(1L));
		verify(jdbcTemplate, times(1)).update(DELETE_PRODUCT_SQL, 1L);
	}

	/**
	 * Update product updates row.
	 */
	@Test
	void updateProduct_UpdatesRow() {
		Product updateRequest = new Product(1, "Phone Pro", 1099.99, null);
		Product storedProduct = new Product(1, "Phone", 999.99, 2);
		when(jdbcTemplate.queryForObject(eq(FIND_PRODUCT_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(1)))
				.thenReturn(storedProduct);
		when(jdbcTemplate.update(UPDATE_PRODUCT_SQL, "Phone Pro", 1099.99, 1)).thenReturn(1);

		Product result = productDaoJdbc.updateProduct(updateRequest);

		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals("Phone Pro", result.getName());
		assertEquals(1099.99, result.getPrice());
		assertEquals(2, result.getCategoryId());
		verify(jdbcTemplate, times(1)).queryForObject(eq(FIND_PRODUCT_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(1));
		verify(jdbcTemplate, times(1)).update(UPDATE_PRODUCT_SQL, "Phone Pro", 1099.99, 1);
	}

	/**
	 * Update product throws when product missing.
	 */
	@Test
	void updateProduct_ThrowsWhenProductMissing() {
		Product updateRequest = new Product(99, "Phone Pro", 1099.99, null);
		when(jdbcTemplate.queryForObject(eq(FIND_PRODUCT_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(99)))
				.thenThrow(new EmptyResultDataAccessException(1));

		ProductNotFoundException exception = assertThrows(
				ProductNotFoundException.class,
				() -> productDaoJdbc.updateProduct(updateRequest)
		);

		assertEquals("Product not found for id: 99", exception.getMessage());
		verify(jdbcTemplate, times(1)).queryForObject(eq(FIND_PRODUCT_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(99));
		verify(jdbcTemplate, never()).update(eq(UPDATE_PRODUCT_SQL), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
	}

	/**
	 * Update product throws when update count is not one.
	 */
	@Test
	void updateProduct_ThrowsWhenUpdateCountIsNotOne() {
		Product updateRequest = new Product(1, "Phone Pro", 1099.99, null);
		Product storedProduct = new Product(1, "Phone", 999.99, 1);
		when(jdbcTemplate.queryForObject(eq(FIND_PRODUCT_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(1)))
				.thenReturn(storedProduct);
		when(jdbcTemplate.update(UPDATE_PRODUCT_SQL, "Phone Pro", 1099.99, 1)).thenReturn(0);

		IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> productDaoJdbc.updateProduct(updateRequest)
		);

		assertEquals("Unable to update product with id: 1", exception.getMessage());
		verify(jdbcTemplate, times(1)).queryForObject(eq(FIND_PRODUCT_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(1));
		verify(jdbcTemplate, times(1)).update(UPDATE_PRODUCT_SQL, "Phone Pro", 1099.99, 1);
	}
}
