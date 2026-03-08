package com.zs.assignment10.dao;

import com.zs.assignment10.model.Product;
import com.zs.assignment10.util.DatabaseManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Product dao jdbc.
 */
public class ProductDaoJdbcImpl implements ProductDao {

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price")));
            }
        } catch (Exception e) {
            System.err.println("Error fetching all products: " + e.getMessage());
        }
        return products;
    }

    @Override
    public boolean cleanUp() {
        String dropSql = "DROP TABLE IF EXISTS products";
        String createSql = """
                CREATE TABLE products (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    price NUMERIC(10, 2) NOT NULL
                )
                """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(dropSql);
            stmt.executeUpdate(createSql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Product findById(Integer id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching product by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null || !exists(product.getId())) {
            // INSERT
            String sql = "INSERT INTO products (name, price) VALUES (?, ?) RETURNING id";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, product.getName());
                stmt.setDouble(2, product.getPrice());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        product.setId(rs.getInt("id"));
                    }
                }
            } catch (Exception e) {
                System.err.println("Error inserting product: " + e.getMessage());
            }
        } else {
            // UPDATE
            String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, product.getName());
                stmt.setDouble(2, product.getPrice());
                stmt.setInt(3, product.getId());
                stmt.executeUpdate();
            } catch (Exception e) {
                System.err.println("Error updating product: " + e.getMessage());
            }
        }
        return product;
    }

    @Override
    public boolean deleteById(Integer id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected > 0){
                return true;
            }
        } catch (Exception e) {
            System.err.println("Error deleting product: " + e.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public boolean exists(Integer id) {
        String sql = "SELECT 1 FROM products WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.err.println("Error checking if product exists: " + e.getMessage());
        }
        return false;
    }
}
