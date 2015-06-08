package pl.poznan.put.cs.dsg.srds.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.cassandra.core.CassandraTemplate;


@Configuration
@PropertySource("file:src/main/resources/config.properties")
public class Config {

    private static Cluster cluster;
    private static Session session;
    private static String address = "127.0.0.1";
    private static String keyspace = "hecuba";

    @Bean
    static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    static CassandraTemplate cassandraTemplate() {
        cluster = Cluster.builder().addContactPoints(address).build();
        session = cluster.connect(keyspace);
        return new CassandraTemplate(session);
    }

}
