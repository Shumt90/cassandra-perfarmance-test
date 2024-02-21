package edu.shum.cassandraperformancetest.test.single_thread.single_key;

import lombok.SneakyThrows;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Generates CSV file for tests
 */
public class CSVGenerator {

    static OutputStream dataFile;

    @SneakyThrows
    public static void main(String[] args) {
        Path path=Path.of("test-data.csv");
        Files.deleteIfExists(path);
        dataFile = Files.newOutputStream(path);

        dataFile.write("id;data\n".getBytes());

        for (int i = 0; i < 10_000_000; i++) {
            append(i,";");
            append(randomData(),"\n");
        }

        dataFile.flush();
    }

    @SneakyThrows
    private static void append(Object v, String delimiter){
        dataFile.write(v.toString().getBytes());
        dataFile.write(delimiter.getBytes());
    }

    private static String randomData(){
        return UUID.randomUUID() +UUID.randomUUID().toString()+ UUID.randomUUID();
    }




}
