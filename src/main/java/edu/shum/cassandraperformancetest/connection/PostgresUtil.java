package edu.shum.cassandraperformancetest.connection;

import edu.shum.cassandraperformancetest.CassandraPerformanceTestApplication;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class PostgresUtil {
    @SneakyThrows
    public static Connection connection(){
        Properties prop = new Properties();
        prop.load(CassandraPerformanceTestApplication.class.getClassLoader().getResourceAsStream("app.properties"));

        String url = prop.getProperty("postgres.url");
        Properties props = new Properties();
        props.setProperty("user", prop.getProperty("postgres.username"));
        props.setProperty("password", prop.getProperty("postgres.password"));
        props.setProperty("ssl", "false");
        return DriverManager.getConnection(url, props);
    }
}
