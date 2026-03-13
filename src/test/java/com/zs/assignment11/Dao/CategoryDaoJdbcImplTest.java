package com.zs.assignment11.Dao;

import com.zs.assignment11.dao.CategoryDaoJdbcImpl;
import com.zs.assignment11.exception.CategoryNotFoundException;
import com.zs.assignment11.model.Category;
import com.zs.assignment11.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Category dao jdbc impl test.
 */
public class CategoryDaoJdbcImplTest {
	private static final String CHECK_CATEGORY_TABLE_EXISTS_SQL =
			"SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'category')";
	private static final String CREATE_CATEGORY_TABLE_SQL = "CREATE TABLE IF NOT EXISTS category (" +
			"id SERIAL PRIMARY KEY, " +
			"name VARCHAR(255) NOT NULL UNIQUE" +
			")";
	private static final String FIND_ALL_CATEGORIES_SQL = "SELECT id, name FROM category ORDER BY id";
	private static final String INSERT_CATEGORY_SQL = "INSERT INTO category (name) VALUES (?)";
	private static final String FIND_CATEGORY_BY_ID_SQL = "SELECT id, name FROM category WHERE id = ?";
	private static final String DELETE_CATEGORY_SQL = "DELETE FROM category WHERE id = ?";
	private static final String UPDATE_CATEGORY_SQL = "UPDATE category SET name = ? WHERE id = ?";
	private static final String CHECK_CATEGORY_EXISTS_SQL = "SELECT COUNT(1) FROM category WHERE id = ?";
	private static final String FIND_PRODUCTS_BY_CATEGORY_ID_SQL =
			"SELECT id, name, price, category_id FROM product WHERE category_id = ? ORDER BY id";

	private JdbcTemplate jdbcTemplate;
	private CategoryDaoJdbcImpl categoryDaoJdbc;

	/**
	 * Sets up.
	 */
	@BeforeEach
	void setUp() {
		jdbcTemplate = mock(JdbcTemplate.class);
		categoryDaoJdbc = new CategoryDaoJdbcImpl(jdbcTemplate);
	}

	/**
	 * Create category table skips create when table exists.
	 */
	@Test
	void createCategoryTable_SkipsCreateWhenTableExists() {
		when(jdbcTemplate.queryForObject(CHECK_CATEGORY_TABLE_EXISTS_SQL, Boolean.class)).thenReturn(true);

		categoryDaoJdbc.CreateCategoryTable();

		verify(jdbcTemplate, times(1)).queryForObject(CHECK_CATEGORY_TABLE_EXISTS_SQL, Boolean.class);
		verify(jdbcTemplate, never()).execute(CREATE_CATEGORY_TABLE_SQL);
	}

	/**
	 * Create category table creates table when missing.
	 */
	@Test
	void createCategoryTable_CreatesTableWhenMissing() {
		when(jdbcTemplate.queryForObject(CHECK_CATEGORY_TABLE_EXISTS_SQL, Boolean.class)).thenReturn(false);

		categoryDaoJdbc.CreateCategoryTable();

		verify(jdbcTemplate, times(1)).queryForObject(CHECK_CATEGORY_TABLE_EXISTS_SQL, Boolean.class);
		verify(jdbcTemplate, times(1)).execute(CREATE_CATEGORY_TABLE_SQL);
	}

	/**
	 * Create category table propagates data access exception.
	 */
	@Test
	void createCategoryTable_PropagatesDataAccessException() {
		when(jdbcTemplate.queryForObject(CHECK_CATEGORY_TABLE_EXISTS_SQL, Boolean.class))
				.thenThrow(new DataAccessResourceFailureException("db down"));

		DataAccessResourceFailureException exception = assertThrows(
				DataAccessResourceFailureException.class,
				() -> categoryDaoJdbc.CreateCategoryTable()
		);

		assertEquals("db down", exception.getMessage());
		verify(jdbcTemplate, times(1)).queryForObject(CHECK_CATEGORY_TABLE_EXISTS_SQL, Boolean.class);
	}

	/**
	 * Find all categories maps result set.
	 */
	@Test
	void findAllCategories_MapsResultSet() {
		when(jdbcTemplate.query(eq(FIND_ALL_CATEGORIES_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any()))
				.thenAnswer(invocation -> {
					RowMapper<Category> rowMapper = invocation.getArgument(1);

					ResultSet firstRow = mock(ResultSet.class);
					when(firstRow.getInt("id")).thenReturn(1);
					when(firstRow.getString("name")).thenReturn("electronics");

					ResultSet secondRow = mock(ResultSet.class);
					when(secondRow.getInt("id")).thenReturn(2);
					when(secondRow.getString("name")).thenReturn("fashion");

					return List.of(
							rowMapper.mapRow(firstRow, 0),
							rowMapper.mapRow(secondRow, 1)
					);
				});

		List<Category> result = categoryDaoJdbc.findAllCategories();

		assertEquals(2, result.size());
		assertEquals("electronics", result.get(0).getName());
		assertEquals("fashion", result.get(1).getName());
		verify(jdbcTemplate, times(1)).query(eq(FIND_ALL_CATEGORIES_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any());
	}

	/**
	 * Add category inserts row.
	 */
	@Test
	void addCategory_InsertsRow() {
		Category category = new Category(1, "electronics");
		when(jdbcTemplate.update(INSERT_CATEGORY_SQL, category.getName())).thenReturn(1);

		Category result = categoryDaoJdbc.addCategory(category);

		assertEquals(1, result.getId());
		assertEquals("electronics", result.getName());
		verify(jdbcTemplate, times(1)).update(INSERT_CATEGORY_SQL, category.getName());
	}

	/**
	 * Add category throws when insert count is not one.
	 */
	@Test
	void addCategory_ThrowsWhenInsertCountIsNotOne() {
		Category category = new Category(1, "electronics");
		when(jdbcTemplate.update(INSERT_CATEGORY_SQL, category.getName())).thenReturn(0);

		IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> categoryDaoJdbc.addCategory(category)
		);

		assertEquals("Unable to insert category: electronics", exception.getMessage());
		verify(jdbcTemplate, times(1)).update(INSERT_CATEGORY_SQL, category.getName());
	}

	/**
	 * Delete category deletes row.
	 */
	@Test
	void deleteCategory_DeletesRow() {
		Category storedCategory = new Category(1, "electronics");
		when(jdbcTemplate.query(eq(FIND_CATEGORY_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any(), eq(1L)))
				.thenReturn(List.of(storedCategory));
		when(jdbcTemplate.update(DELETE_CATEGORY_SQL, 1L)).thenReturn(1);

		Category result = categoryDaoJdbc.deleteCategory(1L);

		assertSame(storedCategory, result);
		verify(jdbcTemplate, times(1)).query(eq(FIND_CATEGORY_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any(), eq(1L));
		verify(jdbcTemplate, times(1)).update(DELETE_CATEGORY_SQL, 1L);
	}

	/**
	 * Delete category throws when delete count is not one.
	 */
	@Test
	void deleteCategory_ThrowsWhenDeleteCountIsNotOne() {
		Category storedCategory = new Category(1, "electronics");
		when(jdbcTemplate.query(eq(FIND_CATEGORY_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any(), eq(1L)))
				.thenReturn(List.of(storedCategory));
		when(jdbcTemplate.update(DELETE_CATEGORY_SQL, 1L)).thenReturn(0);

		IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> categoryDaoJdbc.deleteCategory(1L)
		);

		assertEquals("Unable to delete category: 1", exception.getMessage());
		verify(jdbcTemplate, times(1)).query(eq(FIND_CATEGORY_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any(), eq(1L));
		verify(jdbcTemplate, times(1)).update(DELETE_CATEGORY_SQL, 1L);
	}

	/**
	 * Missing category counts stream.
	 *
	 * @return the stream
	 */
	static Stream<Integer> missingCategoryCounts() {
		return Stream.of(null, 0);
	}

	/**
	 * Find all products by category id throws when category missing.
	 *
	 * @param count the count
	 */
	@ParameterizedTest
	@MethodSource("missingCategoryCounts")
	void findAllProductsByCategoryId_ThrowsWhenCategoryMissing(Integer count) {
		Long categoryId = 99L;
		when(jdbcTemplate.queryForObject(CHECK_CATEGORY_EXISTS_SQL, Integer.class, categoryId)).thenReturn(count);

		CategoryNotFoundException exception = assertThrows(
				CategoryNotFoundException.class,
				() -> categoryDaoJdbc.findAllProductsByCategoryId(categoryId)
		);

		assertEquals("Category not found for id: 99", exception.getMessage());
		verify(jdbcTemplate, times(1)).queryForObject(CHECK_CATEGORY_EXISTS_SQL, Integer.class, categoryId);
		verify(jdbcTemplate, never()).query(eq(FIND_PRODUCTS_BY_CATEGORY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(categoryId));
	}

	/**
	 * Find all products by category id maps result set.
	 */
	@Test
	void findAllProductsByCategoryId_MapsResultSet() {
		Long categoryId = 2L;
		when(jdbcTemplate.queryForObject(CHECK_CATEGORY_EXISTS_SQL, Integer.class, categoryId)).thenReturn(1);
		when(jdbcTemplate.query(eq(FIND_PRODUCTS_BY_CATEGORY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(categoryId)))
				.thenAnswer(invocation -> {
					RowMapper<Product> rowMapper = invocation.getArgument(1);

					ResultSet firstRow = mock(ResultSet.class);
					when(firstRow.getInt("id")).thenReturn(4);
					when(firstRow.getString("name")).thenReturn("FaceCream");
					when(firstRow.getDouble("price")).thenReturn(4.5);
					when(firstRow.getInt("category_id")).thenReturn(2);

					ResultSet secondRow = mock(ResultSet.class);
					when(secondRow.getInt("id")).thenReturn(5);
					when(secondRow.getString("name")).thenReturn("faceGel");
					when(secondRow.getDouble("price")).thenReturn(10.0);
					when(secondRow.getInt("category_id")).thenReturn(2);

					return List.of(
							rowMapper.mapRow(firstRow, 0),
							rowMapper.mapRow(secondRow, 1)
					);
				});

		List<Product> result = categoryDaoJdbc.findAllProductsByCategoryId(categoryId);

		assertEquals(2, result.size());
		assertEquals("FaceCream", result.getFirst().getName());
		assertEquals(4.5, result.getFirst().getPrice());
		assertEquals(2, result.getFirst().getCategoryId());
		assertEquals("faceGel", result.get(1).getName());
		assertEquals(10.0, result.get(1).getPrice());
		assertEquals(2, result.get(1).getCategoryId());
		verify(jdbcTemplate, times(1)).queryForObject(CHECK_CATEGORY_EXISTS_SQL, Integer.class, categoryId);
		verify(jdbcTemplate, times(1)).query(eq(FIND_PRODUCTS_BY_CATEGORY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(categoryId));
	}

	/**
	 * Find all products by category id propagates query exception.
	 */
	@Test
	void findAllProductsByCategoryId_PropagatesQueryException() {
		Long categoryId = 1L;
		when(jdbcTemplate.queryForObject(CHECK_CATEGORY_EXISTS_SQL, Integer.class, categoryId)).thenReturn(1);
		when(jdbcTemplate.query(eq(FIND_PRODUCTS_BY_CATEGORY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(categoryId)))
				.thenThrow(new DataAccessResourceFailureException("query failed"));

		DataAccessResourceFailureException exception = assertThrows(
				DataAccessResourceFailureException.class,
				() -> categoryDaoJdbc.findAllProductsByCategoryId(categoryId)
		);

		assertTrue(exception.getMessage().contains("query failed"));
		verify(jdbcTemplate, times(1)).queryForObject(CHECK_CATEGORY_EXISTS_SQL, Integer.class, categoryId);
		verify(jdbcTemplate, times(1)).query(eq(FIND_PRODUCTS_BY_CATEGORY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Product>>any(), eq(categoryId));
	}

	/**
	 * Update category updates row.
	 */
	@Test
	void updateCategory_UpdatesRow() {
		Category updateRequest = new Category(1, "fashion");
		when(jdbcTemplate.query(eq(FIND_CATEGORY_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any(), eq(1)))
				.thenReturn(List.of(new Category(1, "electronics")));
		when(jdbcTemplate.update(UPDATE_CATEGORY_SQL, "fashion", 1)).thenReturn(1);

		Category result = categoryDaoJdbc.updateCategory(updateRequest);

		assertEquals(1, result.getId());
		assertEquals("fashion", result.getName());
		verify(jdbcTemplate, times(1)).query(eq(FIND_CATEGORY_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any(), eq(1));
		verify(jdbcTemplate, times(1)).update(UPDATE_CATEGORY_SQL, "fashion", 1);
	}

	/**
	 * Update category throws when category missing.
	 */
	@Test
	void updateCategory_ThrowsWhenCategoryMissing() {
		Category updateRequest = new Category(99, "fashion");
		when(jdbcTemplate.query(eq(FIND_CATEGORY_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any(), eq(99)))
				.thenReturn(List.of());

		CategoryNotFoundException exception = assertThrows(
				CategoryNotFoundException.class,
				() -> categoryDaoJdbc.updateCategory(updateRequest)
		);

		assertEquals("Category not found for id: 99", exception.getMessage());
		verify(jdbcTemplate, times(1)).query(eq(FIND_CATEGORY_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any(), eq(99));
		verify(jdbcTemplate, never()).update(eq(UPDATE_CATEGORY_SQL), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
	}

	/**
	 * Update category throws when update count is not one.
	 */
	@Test
	void updateCategory_ThrowsWhenUpdateCountIsNotOne() {
		Category updateRequest = new Category(1, "fashion");
		when(jdbcTemplate.query(eq(FIND_CATEGORY_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any(), eq(1)))
				.thenReturn(List.of(new Category(1, "electronics")));
		when(jdbcTemplate.update(UPDATE_CATEGORY_SQL, "fashion", 1)).thenReturn(0);

		IllegalStateException exception = assertThrows(
				IllegalStateException.class,
				() -> categoryDaoJdbc.updateCategory(updateRequest)
		);

		assertEquals("Unable to update category with id: 1", exception.getMessage());
		verify(jdbcTemplate, times(1)).query(eq(FIND_CATEGORY_BY_ID_SQL), org.mockito.ArgumentMatchers.<RowMapper<Category>>any(), eq(1));
		verify(jdbcTemplate, times(1)).update(UPDATE_CATEGORY_SQL, "fashion", 1);
	}
}
