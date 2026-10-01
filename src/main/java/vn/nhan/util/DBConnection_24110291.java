package vn.nhan.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection_24110291 {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = DBConnection_24110291.class.getClassLoader().getResourceAsStream("db.properties")) {
            PROPS.load(in);
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"), PROPS.getProperty("db.password"));
    }
}
