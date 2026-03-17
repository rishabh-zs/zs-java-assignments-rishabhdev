package com.zs.assignment10.dao;

import com.zs.assignment10.model.Product;
import com.zs.assignment10.util.DatabaseManager;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Product dao JDBC.
 */
@Slf4j
public class ProductDaoJdbcImpl implements ProductDao {

    @Override
    public List<Product> findAll() throws Exception {
        logger.info("<-- Executing Get all products SQL statement");
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price")));
            }
            return products;
        } catch (Exception e) {
            throw new Exception("Error fetching all products from database: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean cleanUp() throws Exception {
        logger.info("<-- Executing schema initialization SQL statement");
        String createSql = """
                CREATE TABLE IF NOT EXISTS products (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    price NUMERIC(10, 2) NOT NULL
                )
                """;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(createSql);
            return true;
        } catch (Exception e) {
            logger.error("Error ensuring products table exists: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Product findById(Integer id) throws Exception {
        logger.info("<-- Executing Get product SQL statement");
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
            throw new Exception("Error fetching product by id: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Product deleteById(Integer id) throws Exception {
        logger.info("<-- Executing Deleting product SQL statement");
        String sql = "DELETE FROM products WHERE id = ? RETURNING id, name, price";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Product(rs.getInt("id"), rs.getString("name"), rs.getDouble("price"));
                }
            }
        } catch (Exception e) {
            throw new Exception("Error deleting product: " + e.getMessage(), e);
        }
        return null;
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
            logger.error("Error checking if product exists: {}", e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Product insert(Product product) throws Exception {
        logger.info("<-- Executing insert product SQL statement");
        String sql = "INSERT INTO products (name, price) VALUES (?, ?) RETURNING id";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstat = conn.prepareStatement(sql)) {

            pstat.setString(1, product.getName());
            pstat.setDouble(2, product.getPrice());

            try (ResultSet rs = pstat.executeQuery()) {
                if (rs.next()) {
                    product.setId(rs.getInt("id"));
                    return product;
                }
            }
        } catch (Exception e) {
            throw new Exception("Error inserting product: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Product update(Product product) throws Exception {
        logger.info("<-- Executing update product SQL statement");
        if (!exists(product.getId())) {
            logger.info("<-- does not exists, cannot update!");
            return null;
        } else {
            String sql = "UPDATE products SET name = ?, price = ? WHERE id = ? RETURNING id, name, price";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement pstat = conn.prepareStatement(sql)) {

                pstat.setString(1, product.getName());
                pstat.setDouble(2, product.getPrice());
                pstat.setInt(3, product.getId());

                try (ResultSet rs = pstat.executeQuery()) {
                    if (rs.next()) {
                        product.setId(rs.getInt("id"));
                        return product;
                    }
                }
            } catch (Exception e) {
                throw new Exception("Error updating product: " + e.getMessage(), e);
            }
        }
        return null;
    }
}
