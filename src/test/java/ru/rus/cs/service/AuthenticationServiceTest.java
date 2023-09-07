package ru.rus.cs.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.rus.cs.db.model.UserTable;
import ru.rus.cs.repository.AuthenticationRepository;
import ru.rus.cs.security.JwtTokenUtil;
import ru.rus.cs.web.model.AuthenticationRequest;
import ru.rus.cs.web.model.AuthenticationResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing authentication service functionality.")
public class AuthenticationServiceTest {


    @InjectMocks
    AuthenticationService authenticationService;

    @Mock
    AuthenticationRepository authenticationRepository;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserService userService;

    @Mock
    JwtTokenUtil jwtTokenUtil;



    public static final String USERNAME = "rus@mail.ru";
    public static final String PASSWORD = "ruslan";
    public static final String TOKEN = "Token";
    public static final String BEARER_TOKEN = "Bearer Token";
    public static final AuthenticationResponse AUTHORIZATION_RESPONSE = new AuthenticationResponse(TOKEN);
    public static final AuthenticationRequest AUTHORIZATION_REQUEST = new AuthenticationRequest(USERNAME, PASSWORD);


    @Test
    @DisplayName("Авторизация пользователя. Должно пройти успешно.")
    void login_Test() {
        //given

        UserDetails userDetails = new UserTable(1L,"rus@mail.ru", "rus");

        //when
        when(userService.loadUserByUsername(USERNAME)).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn(TOKEN);

        //then
        AuthenticationResponse expected = AUTHORIZATION_RESPONSE;
        AuthenticationResponse result = authenticationService.signIn(AUTHORIZATION_REQUEST);
        assertEquals(expected,result);
        Mockito.verify(authenticationManager, Mockito.times(1)).authenticate(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD));
        Mockito.verify(authenticationRepository, Mockito.times(1)).putTokenAndUsername(TOKEN, USERNAME);
    }

    @Test
    @DisplayName("Logout. Должно пройти успешно.")
    void logout_Test() {

        when(authenticationRepository.getUserNameByToken(BEARER_TOKEN.substring(7))).thenReturn(USERNAME);
        authenticationService.signOut(BEARER_TOKEN);
        Mockito.verify(authenticationRepository, Mockito.times(1)).getUserNameByToken(BEARER_TOKEN.substring(7));
        Mockito.verify(authenticationRepository, Mockito.times(1)).removeTokenAndUsernameByToken(BEARER_TOKEN.substring(7));

    }

}
