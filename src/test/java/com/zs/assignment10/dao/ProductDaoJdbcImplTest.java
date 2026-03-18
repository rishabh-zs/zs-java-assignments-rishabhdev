package com.zs.assignment10.dao;

import com.zs.assignment10.model.Product;
import com.zs.assignment10.util.DatabaseManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Product dao JDBC impl test.
 */
@ExtendWith(MockitoExtension.class)
public class ProductDaoJdbcImplTest {

    private final ProductDaoJdbcImpl dao = new ProductDaoJdbcImpl();
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;
    @Mock
    private Statement statement;

    /**
     * Find all when rows exist returns mapped products.
     *
     * @throws Exception the exception
     */
    @Test
    void findAll_WhenRowsExist_ReturnsMappedProducts() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("SELECT * FROM products")).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true, true, false);
            when(resultSet.getInt("id")).thenReturn(1, 2);
            when(resultSet.getString("name")).thenReturn("Laptop", "Mouse");
            when(resultSet.getDouble("price")).thenReturn(1200.0, 25.5);

            List<Product> products = dao.findAll();

            assertEquals(2, products.size());
            assertEquals("Laptop", products.get(0).getName());
            assertEquals("Mouse", products.get(1).getName());
        }
    }

    /**
     * Find all when no rows returns empty list.
     *
     * @throws Exception the exception
     */
    @Test
    void findAll_WhenNoRows_ReturnsEmptyList() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("SELECT * FROM products")).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            List<Product> products = dao.findAll();

            assertTrue(products.isEmpty());
        }
    }

    /**
     * Find all when query fails throws wrapped exception.
     *
     * @throws Exception the exception
     */
    @Test
    void findAll_WhenQueryFails_ThrowsWrappedException() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("SELECT * FROM products")).thenThrow(new RuntimeException("boom"));

            Exception ex = assertThrows(Exception.class, () -> dao.findAll());

            assertTrue(ex.getMessage().contains("Error fetching all products"));
        }
    }

    /**
     * Clean up when create table succeeds returns true.
     *
     * @throws Exception the exception
     */
    @Test
    void cleanUp_WhenCreateTableSucceeds_ReturnsTrue() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenReturn(statement);
            when(statement.executeUpdate(anyString())).thenReturn(0);

            assertTrue(dao.cleanUp());
        }
    }

    /**
     * Clean up when create table fails returns false.
     *
     * @throws Exception the exception
     */
    @Test
    void cleanUp_WhenCreateTableFails_ReturnsFalse() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.createStatement()).thenThrow(new RuntimeException("cannot create statement"));

            assertFalse(dao.cleanUp());
        }
    }

    /**
     * Find by id when row exists returns product.
     *
     * @throws Exception the exception
     */
    @Test
    void findById_WhenRowExists_ReturnsProduct() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("SELECT * FROM products WHERE id = ?")).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(7);
            when(resultSet.getString("name")).thenReturn("Desk");
            when(resultSet.getDouble("price")).thenReturn(250.0);

            Product product = dao.findById(7);

            assertNotNull(product);
            assertEquals(7, product.getId());
            assertEquals("Desk", product.getName());
            assertEquals(250.0, product.getPrice());
            verify(preparedStatement).setInt(1, 7);
        }
    }

    /**
     * Find by id when row does not exist returns null.
     *
     * @throws Exception the exception
     */
    @Test
    void findById_WhenRowDoesNotExist_ReturnsNull() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("SELECT * FROM products WHERE id = ?")).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            assertNull(dao.findById(99));
        }
    }

    /**
     * Find by id when query fails throws wrapped exception.
     *
     * @throws Exception the exception
     */
    @Test
    void findById_WhenQueryFails_ThrowsWrappedException() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("SELECT * FROM products WHERE id = ?")).thenThrow(new RuntimeException("fail"));

            Exception ex = assertThrows(Exception.class, () -> dao.findById(1));

            assertTrue(ex.getMessage().contains("Error fetching product by id"));
        }
    }

    /**
     * Delete by id when row deleted returns deleted product.
     *
     * @throws Exception the exception
     */
    @Test
    void deleteById_WhenRowDeleted_ReturnsDeletedProduct() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("DELETE FROM products WHERE id = ? RETURNING id, name, price"))
                    .thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(2);
            when(resultSet.getString("name")).thenReturn("Mouse");
            when(resultSet.getDouble("price")).thenReturn(25.5);

            Product deleted = dao.deleteById(2);

            assertNotNull(deleted);
            assertEquals(2, deleted.getId());
            verify(preparedStatement).setInt(1, 2);
        }
    }

    /**
     * Delete by id when no row deleted returns null.
     *
     * @throws Exception the exception
     */
    @Test
    void deleteById_WhenNoRowDeleted_ReturnsNull() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("DELETE FROM products WHERE id = ? RETURNING id, name, price"))
                    .thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            assertNull(dao.deleteById(88));
        }
    }

    /**
     * Delete by id when delete fails throws wrapped exception.
     *
     * @throws Exception the exception
     */
    @Test
    void deleteById_WhenDeleteFails_ThrowsWrappedException() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("DELETE FROM products WHERE id = ? RETURNING id, name, price"))
                    .thenThrow(new RuntimeException("fail"));

            Exception ex = assertThrows(Exception.class, () -> dao.deleteById(1));

            assertTrue(ex.getMessage().contains("Error deleting product"));
        }
    }

    /**
     * Exists when row found returns true.
     *
     * @throws Exception the exception
     */
    @Test
    void exists_WhenRowFound_ReturnsTrue() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("SELECT 1 FROM products WHERE id = ?")).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);

            assertTrue(dao.exists(1));
            verify(preparedStatement).setInt(1, 1);
        }
    }

    /**
     * Exists when row not found returns false.
     *
     * @throws Exception the exception
     */
    @Test
    void exists_WhenRowNotFound_ReturnsFalse() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("SELECT 1 FROM products WHERE id = ?")).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            assertFalse(dao.exists(404));
        }
    }

    /**
     * Exists when query fails returns false.
     *
     * @throws Exception the exception
     */
    @Test
    void exists_WhenQueryFails_ReturnsFalse() throws Exception {
        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenThrow(new RuntimeException("db unavailable"));

            assertFalse(dao.exists(1));
        }
    }

    /**
     * Insert when insert succeeds returns product with generated id.
     *
     * @throws Exception the exception
     */
    @Test
    void insert_WhenInsertSucceeds_ReturnsProductWithGeneratedId() throws Exception {
        Product product = new Product(null, "Keyboard", 75.0);

        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("INSERT INTO products (name, price) VALUES (?, ?) RETURNING id"))
                    .thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("id")).thenReturn(10);

            Product inserted = dao.insert(product);

            assertNotNull(inserted);
            assertEquals(10, inserted.getId());
            assertEquals("Keyboard", inserted.getName());
            verify(preparedStatement).setString(1, "Keyboard");
            verify(preparedStatement).setDouble(2, 75.0);
        }
    }

    /**
     * Insert when no generated id returned returns null.
     *
     * @throws Exception the exception
     */
    @Test
    void insert_WhenNoGeneratedIdReturned_ReturnsNull() throws Exception {
        Product product = new Product(null, "Keyboard", 75.0);

        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("INSERT INTO products (name, price) VALUES (?, ?) RETURNING id"))
                    .thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            assertNull(dao.insert(product));
        }
    }

    /**
     * Insert when insert fails throws wrapped exception.
     *
     * @throws Exception the exception
     */
    @Test
    void insert_WhenInsertFails_ThrowsWrappedException() throws Exception {
        Product product = new Product(null, "Keyboard", 75.0);

        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("INSERT INTO products (name, price) VALUES (?, ?) RETURNING id"))
                    .thenThrow(new RuntimeException("insert fail"));

            Exception ex = assertThrows(Exception.class, () -> dao.insert(product));

            assertTrue(ex.getMessage().contains("Error inserting product"));
        }
    }

    /**
     * Update when product exists and update succeeds returns updated product.
     *
     * @throws Exception the exception
     */
    @Test
    void update_WhenProductExistsAndUpdateSucceeds_ReturnsUpdatedProduct() throws Exception {
        Product product = new Product(3, "Monitor", 300.0);

        Connection existsConnection = mock(Connection.class);
        PreparedStatement existsStatement = mock(PreparedStatement.class);
        ResultSet existsResultSet = mock(ResultSet.class);

        Connection updateConnection = mock(Connection.class);
        PreparedStatement updateStatement = mock(PreparedStatement.class);
        ResultSet updateResultSet = mock(ResultSet.class);

        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(existsConnection, updateConnection);

            when(existsConnection.prepareStatement("SELECT 1 FROM products WHERE id = ?")).thenReturn(existsStatement);
            when(existsStatement.executeQuery()).thenReturn(existsResultSet);
            when(existsResultSet.next()).thenReturn(true);

            when(updateConnection.prepareStatement("UPDATE products SET name = ?, price = ? WHERE id = ? RETURNING id, name, price"))
                    .thenReturn(updateStatement);
            when(updateStatement.executeQuery()).thenReturn(updateResultSet);
            when(updateResultSet.next()).thenReturn(true);
            when(updateResultSet.getInt("id")).thenReturn(3);

            Product updated = dao.update(product);

            assertNotNull(updated);
            assertEquals(3, updated.getId());
            assertEquals("Monitor", updated.getName());
            assertEquals(300.0, updated.getPrice());
            verify(updateStatement).setString(1, "Monitor");
            verify(updateStatement).setDouble(2, 300.0);
            verify(updateStatement).setInt(3, 3);
        }
    }

    /**
     * Update when product does not exist returns null.
     *
     * @throws Exception the exception
     */
    @Test
    void update_WhenProductDoesNotExist_ReturnsNull() throws Exception {
        Product product = new Product(999, "Ghost", 1.0);

        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(connection);
            when(connection.prepareStatement("SELECT 1 FROM products WHERE id = ?")).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(resultSet.next()).thenReturn(false);

            assertNull(dao.update(product));
        }
    }

    /**
     * Update when update fails throws wrapped exception.
     *
     * @throws Exception the exception
     */
    @Test
    void update_WhenUpdateFails_ThrowsWrappedException() throws Exception {
        Product product = new Product(4, "Tablet", 500.0);

        Connection existsConnection = mock(Connection.class);
        PreparedStatement existsStatement = mock(PreparedStatement.class);
        ResultSet existsResultSet = mock(ResultSet.class);

        Connection updateConnection = mock(Connection.class);

        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(existsConnection, updateConnection);

            when(existsConnection.prepareStatement("SELECT 1 FROM products WHERE id = ?")).thenReturn(existsStatement);
            when(existsStatement.executeQuery()).thenReturn(existsResultSet);
            when(existsResultSet.next()).thenReturn(true);

            when(updateConnection.prepareStatement("UPDATE products SET name = ?, price = ? WHERE id = ? RETURNING id, name, price"))
                    .thenThrow(new RuntimeException("update fail"));

            Exception ex = assertThrows(Exception.class, () -> dao.update(product));

            assertTrue(ex.getMessage().contains("Error updating product"));
        }
    }

    /**
     * Update when update returns no row returns null.
     *
     * @throws Exception the exception
     */
    @Test
    void update_WhenUpdateReturnsNoRow_ReturnsNull() throws Exception {
        Product product = new Product(6, "Pen", 2.5);

        Connection existsConnection = mock(Connection.class);
        PreparedStatement existsStatement = mock(PreparedStatement.class);
        ResultSet existsResultSet = mock(ResultSet.class);

        Connection updateConnection = mock(Connection.class);
        PreparedStatement updateStatement = mock(PreparedStatement.class);
        ResultSet updateResultSet = mock(ResultSet.class);

        try (MockedStatic<DatabaseManager> db = mockStatic(DatabaseManager.class)) {
            db.when(DatabaseManager::getConnection).thenReturn(existsConnection, updateConnection);

            when(existsConnection.prepareStatement("SELECT 1 FROM products WHERE id = ?")).thenReturn(existsStatement);
            when(existsStatement.executeQuery()).thenReturn(existsResultSet);
            when(existsResultSet.next()).thenReturn(true);

            when(updateConnection.prepareStatement("UPDATE products SET name = ?, price = ? WHERE id = ? RETURNING id, name, price"))
                    .thenReturn(updateStatement);
            when(updateStatement.executeQuery()).thenReturn(updateResultSet);
            when(updateResultSet.next()).thenReturn(false);

            assertNull(dao.update(product));
        }
    }
}

