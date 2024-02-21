package edu.shum.cassandraperformancetest.test.single_thread.single_key;

import edu.shum.cassandraperformancetest.connection.PostgresUtil;
import lombok.SneakyThrows;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostgresTest {

    public static void main(String[] args) throws IOException, SQLException {
        System.out.println("Connecting...");
        Connection conn = PostgresUtil.connection();

        PreparedStatement ps = conn.prepareStatement("select * from perf.t_single where id=?");

        var counter=new AtomicInteger();
        List<Integer> ids = Stream.generate(counter::incrementAndGet)
            .limit(1000)
            .collect(Collectors.toList());

        long time = System.nanoTime();
        long count = ids.stream()
            .map(id -> execute(ps, id))
            .filter(v -> v)
            .count();
        long end = System.nanoTime();
        System.out.println("Counts of record read: " + count);
        System.out.println("Test time(sec): " + (((double) end - time) / 1_000_000_000));

        conn.close();
    }

    @SneakyThrows
    private static boolean execute(PreparedStatement ps, Integer id) {
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        return rs.next() && rs.getString("id") != null;
    }
}
