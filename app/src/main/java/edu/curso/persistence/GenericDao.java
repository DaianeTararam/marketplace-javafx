package edu.curso.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GenericDao {
    private Connection c;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        String hostName = "localhost";
        String dbName   = "db_ArteCRAFT";
        String user     = "usuario";
        String senha    = "123456";

        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        c = DriverManager.getConnection(
            "jdbc:sqlserver://" + hostName + ":1433;" +
            "databaseName=" + dbName + ";" +
            "user=" + user + ";" +
            "password=" + senha + ";" +
            "encrypt=false;"
        );

        return c;
    }
    	
}
