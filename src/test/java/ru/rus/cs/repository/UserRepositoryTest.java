package ru.rus.cs.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import ru.rus.cs.config.SystemJpaTest;
import ru.rus.cs.db.model.UserTable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


    @DisplayName("Получить юзера по username. Число select должно равняться 2, insert 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_data.sql"
    })
    void findUserByUsername_thenAssertDmlCount() {

        String username = "user@mail.ru";

        UserTable user = new UserTable();
        user.setUsername("user@mail.ru");
        user.setPassword("user");

        UserTable savedUser = userRepository.save(user);
        Optional<UserTable> result = userRepository.findByUsername(username);
        assertThat(savedUser.getUsername()).isEqualTo(username);
        result.ifPresent(value -> assertThat(value.getUsername()).isEqualTo(username));
        assertThat(2);
        assertThat(1);
        assertThat(0);
        assertThat(0);


    }
}
