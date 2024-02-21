# Comparison of Cassandra(LSM tree) vs Postgres(BTree)
Reading by LSM tree is faster than BTree, but how much?

## Cassandra
| Data size | Cache   | Compaction strategy          | SSTable count | Compression | Command                            | Result(sec) |
|-----------|---------|------------------------------|---------------|-------------|------------------------------------|-------------|
| 10Gb      | Cleaned | SizeTieredCompactionStrategy | 1             | ON          | SINGLE_THREAD_SIMPLE_KEY_CASSANDRA | 0.49-0.53   |
| 10Gb      | Cleaned | SizeTieredCompactionStrategy | 5             | ON          | SINGLE_THREAD_SIMPLE_KEY_CASSANDRA | 0.49-0.54   |
| 10Gb      | Cleaned | SizeTieredCompactionStrategy | 1             | OFF         | SINGLE_THREAD_SIMPLE_KEY_CASSANDRA | 0.48-0.52   |

All tests were run by command like:
```shell
for run in {1..10}; do nodetool invalidatekeycache && java -jar /opt/tests.jar SINGLE_THREAD_SIMPLE_KEY_CASSANDRA 10000|grep "Test time">>result.txt; done
```

## Postgres

| Data size | Cache | Test                              | Result(sec) +-0.05 |
|-----------|-------|-----------------------------------|--------------------|
| 1Gb       |Cleaned| SINGLE_THREAD_SIMPLE_KEY_POSTGRES | 0.2                |
| 10Gb      |Cleaned| SINGLE_THREAD_SIMPLE_KEY_POSTGRES | 0.2                |

# Conclusion
Single thread reading on single node is two times faster in Postgres. 

# Useful links


# Command helper:
### Create schema
Schemas are located in resources

### CSVGenerator class helps to generate test data
### Sending data to a server
```
scp test-data.csv $SERVER_USER@$SERVER_IP:/opt/cassandra/test-data.csv 
```
### Load a data
#### Cassandra
```
time cqlsh $SERVER_IP -k perf -e "COPY t_single FROM '/opt/cassandra/test-data.csv' WITH DELIMITER = ';' AND HEADER = TRUE"
```
#### Postgres
``` shell
psql -h $SERVER_IP -d perf -U postgres -W
```
``` sql
select current_timestamp;
COPY perf.t_single(id, data)
FROM '/opt/cassandra/test-data.csv'
DELIMITER ';'
CSV HEADER;
select current_timestamp;
```
### Build a jar
```shell
mvn clean package
```
### Sending to a server
```shell
scp target/cassandra-perfarmance-test-0.0.1-SNAPSHOT-jar-with-dependencies.jar $SERVER_USER@$SERVER_IP:/opt/tests.jar
```
### Compact
```shell
nodetool compact perf
```
### See SSTables 
```shell
sstableutil -t all perf t_single
```
### Clear cache Cassandra
```shell
nodetool flush perf
nodetool invalidatekeycache;
```
### Clean Cassandra
```shell
nodetool cleanup;
```
### Clear cache Postgres
```shell
sudo service postgresql stop
sudo sh -c "sudo sync; /usr/bin/echo 1 > /proc/sys/vm/drop_caches"
sudo sh -c "sudo sync; /usr/bin/echo 2 > /proc/sys/vm/drop_caches"
sudo sh -c "sudo sync; /usr/bin/echo 3 > /proc/sys/vm/drop_caches"
sudo swapoff -a
sudo service postgresql start
```

# Tests
```shell
java -jar /opt/tests.jar SINGLE_THREAD_SIMPLE_KEY_CASSANDRA
java -jar /opt/tests.jar SINGLE_THREAD_SIMPLE_KEY_POSTGRES
```
