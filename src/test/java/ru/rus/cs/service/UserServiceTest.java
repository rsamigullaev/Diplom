package ru.rus.cs.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rus.cs.db.model.UserTable;
import ru.rus.cs.repository.UserRepository;

import java.util.Optional;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing User service functionality.")
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("Получить пользователя по Username. Должно пройти успешно.")
    void loadUserByUsername_Test() {

        //given
        String username = "rus@mail.ru";
        UserTable expected = new UserTable(1L, "rus@mail.ru", "rus");

        //when
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(expected))
                .thenThrow(new UsernameNotFoundException(
                        format("User with username - %s, not found", username)));
        //then
        UserDetails result = userService.loadUserByUsername(username);
        assertEquals(expected, result);

    }
}
