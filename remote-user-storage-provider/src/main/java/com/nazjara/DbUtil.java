package com.nazjara;

import org.keycloak.component.ComponentModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

    public static Connection getConnection(ComponentModel config) throws SQLException {
        var driverClass = config.get("CONFIG_KEY_JDBC_DRIVER");
        try {
            Class.forName(driverClass);
        }
        catch(ClassNotFoundException nfe) {
            // ... error handling omitted
        }

        return DriverManager.getConnection(
                config.get("CONFIG_KEY_JDBC_URL"),
                config.get("CONFIG_KEY_DB_USERNAME"),
                config.get("CONFIG_KEY_DB_PASSWORD"));
    }
}
