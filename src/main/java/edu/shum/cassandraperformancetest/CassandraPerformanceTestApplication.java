package edu.shum.cassandraperformancetest;


import edu.shum.cassandraperformancetest.test.Tests;
import edu.shum.cassandraperformancetest.test.single_thread.single_key.PostgresTest;

public class CassandraPerformanceTestApplication {

    public static void main(String[] args) throws Exception {

        String testName = args[0];

        var test = Tests.valueOf(testName);
        if (test == Tests.SINGLE_THREAD_SIMPLE_KEY_CASSANDRA) {
            edu.shum.cassandraperformancetest.test.single_thread.single_key.CassandraTest.main(args );
        } if (test == Tests.MULTY_THREAD_CASSANDRA) {
            edu.shum.cassandraperformancetest.test.multy_thread.CassandraTest.main(args);
        }else if (test == Tests.SINGLE_THREAD_SIMPLE_KEY_POSTGRES) {
            PostgresTest.main(null);
        }
    }

}
