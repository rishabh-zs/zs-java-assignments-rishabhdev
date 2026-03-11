package com.zs.assignment7.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The type Database connection manager.
 */
public class DatabaseConnectionManager {
    private static final String URL = "jdbc:postgresql://localhost:5435/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "User#2026";
    private static Connection connection;


    /**
     * Connect connection.
     *
     * @return the connection
     * @throws SQLException the SQL exception
     */
    public static synchronized Connection Connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}