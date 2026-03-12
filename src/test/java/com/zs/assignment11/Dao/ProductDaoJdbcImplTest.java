package com.zs.assignment11.Dao;

import com.zs.assignment11.dao.ProductDaoJdbcImpl;
import com.zs.assignment11.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
