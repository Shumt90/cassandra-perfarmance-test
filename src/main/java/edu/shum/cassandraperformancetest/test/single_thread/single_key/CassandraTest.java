package edu.shum.cassandraperformancetest.test.single_thread.single_key;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import edu.shum.cassandraperformancetest.connection.CassandraUtil;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CassandraTest {

    public static void main(String[] args) {
        int step = Integer.parseInt(args[1]);
        System.out.println("Connecting...");
        Session session = CassandraUtil.session();
        var counter = new AtomicInteger();

        List<Integer> ids = Stream.generate(()->counter.getAndAdd(step))
            .limit(1000)
            .collect(Collectors.toList());

        PreparedStatement ps = session.prepare("select * from t_single where id=?");

        System.out.println("Start reading");
        long time = System.nanoTime();
        long count = ids.stream()
            .map(id -> {
                ResultSet rs = session.execute(ps.bind(id)
//                    .enableTracing()
                );
                return rs.all().size() == 1;
            })
            .filter(v -> v)
            .count();
        long end = System.nanoTime();
        System.out.println("Counts of record read: " + count);
        System.out.println("Test time(sec): " + (((double) end - time) / 1_000_000_000));

        System.exit(0);
    }
}
