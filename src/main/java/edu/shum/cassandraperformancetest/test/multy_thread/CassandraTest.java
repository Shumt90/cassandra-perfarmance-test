package edu.shum.cassandraperformancetest.test.multy_thread;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import edu.shum.cassandraperformancetest.connection.CassandraUtil;
import lombok.SneakyThrows;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CassandraTest {


    @SneakyThrows
    public static void main(String[] args) {
        int step = Integer.parseInt(args[1]);
        int parallelism = Integer.parseInt(args[2]);
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        var counter = new AtomicInteger();
        for (int i = 0; i < 1000; i++) {
            int cur = counter.addAndGet(step);
            queue.add(cur);
        }

        List<Thread> threads=new LinkedList<>();
        AtomicInteger read=new AtomicInteger();
        for (int i = 0; i < parallelism; i++) {
            threads.add(new Thread(() -> {
                System.out.printf("Thread %s, connecting...%n", Thread.currentThread().getId());
                Session session = CassandraUtil.session();
                PreparedStatement ps = session.prepare("select * from t_single where id=?");
                while (!queue.isEmpty()){
                    Integer val=queue.poll();
                    if(val==null){
                        break;
                    }
                    ResultSet rs = session.execute(ps.bind(val));
                    if(rs.all().size() == 1){
                        read.incrementAndGet();
                    }
                }
            },String.valueOf(i)));
        }

        long time = System.nanoTime();

        for(var thread:threads){
            thread.start();
        }

        for(var thread:threads){
            thread.join();
        }

        long end = System.nanoTime();
        System.out.println("Counts of record read: " + read.get());
        System.out.println("Test time(sec): " + (((double) end - time) / 1_000_000_000));

        System.exit(0);
    }
}
