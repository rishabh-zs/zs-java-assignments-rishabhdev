package com.zs.assignment10.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * The type Database manager.
 */
public class DatabaseManager {
    private static final Properties props = new Properties();
    private static Connection connection;

    static {
        try (InputStream in = DatabaseManager.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) throw new RuntimeException("db.properties not found on classpath");
            props.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database properties", e);
        }
    }

    /**
     * Gets connection.
     *
     * @return the connection
     * @throws Exception the exception
     */
    public static Connection getConnection() throws Exception {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );
        }
        return connection;
    }
}
