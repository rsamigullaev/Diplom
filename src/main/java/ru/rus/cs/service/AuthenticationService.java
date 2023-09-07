package ru.rus.cs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rus.cs.repository.AuthenticationRepository;
import ru.rus.cs.repository.UserRepository;
import ru.rus.cs.security.JwtTokenUtil;
import ru.rus.cs.web.model.AuthenticationRequest;
import ru.rus.cs.web.model.AuthenticationResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationRepository authenticationRepository;
    private final UserRepository userRepository;

    public AuthenticationResponse signIn(final AuthenticationRequest request) {
        final String username = request.login();
        final String password = request.password();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        final UserDetails userDetails = userService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(userDetails);
        authenticationRepository.putTokenAndUsername(token, username);
        log.info("User {} is authorized", username);
        return new AuthenticationResponse(token);
    }

    public void signOut(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
        }
        final String username = authenticationRepository.getUserNameByToken(authToken);
        log.info("User {} logout", username);
        authenticationRepository.removeTokenAndUsernameByToken(authToken);
    }
}
