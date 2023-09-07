package ru.rus.cs.config;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

@TestConfiguration
@Testcontainers
public class PostgreSqlContainerConfig {

    private static final String IMAGE_VERSION = "postgres:alpine";
    private static final String DB_NAME = "postgres";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String POSTGRESQL_DRIVER_CLASS_NAME = "org.postgresql.Driver";
    private static final String JDBC_URL_FORMAT = "jdbc:postgresql://localhost:5432/postgres";

    private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(
            DockerImageName.parse(IMAGE_VERSION).asCompatibleSubstituteFor("postgres"))
            .withDatabaseName(DB_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD);

    static {
        POSTGRES.start();
    }

    @Bean
    public DataSource dataSource() {
        String connectionUrl = String.format(JDBC_URL_FORMAT,
                POSTGRES.getContainerIpAddress(),
                POSTGRES.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT),
                POSTGRES.getDatabaseName());

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(POSTGRESQL_DRIVER_CLASS_NAME);
        dataSource.setUrl(connectionUrl);
        dataSource.setUsername(POSTGRES.getUsername());
        dataSource.setPassword(POSTGRES.getPassword());

        return dataSource;
    }

}
