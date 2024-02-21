package edu.shum.cassandraperformancetest.connection;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import edu.shum.cassandraperformancetest.CassandraPerformanceTestApplication;
import lombok.SneakyThrows;

import java.util.Properties;

public class CassandraUtil {
    @SneakyThrows
    public static Session session(){
        Properties prop = new Properties();
        prop.load(CassandraPerformanceTestApplication.class.getClassLoader().getResourceAsStream("app.properties"));
        Cluster cluster = Cluster.builder()
            .addContactPoint(prop.getProperty("cassandra.url"))
            .build();
        return cluster.connect("perf");
    }
}
