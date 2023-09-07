package ru.rus.cs.config;


import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("ru.rus.cs.db.model")
@EnableJpaRepositories(basePackages = {"ru.rus.cs.repository"})
@ComponentScan({"ru.rus.cs.repository"})
public class SystemTestingJpaConfig {
}
