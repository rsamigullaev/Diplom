package ru.rus.cs.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.rus.cs.service.AuthenticationService;
import ru.rus.cs.web.model.AuthenticationRequest;
import ru.rus.cs.web.model.AuthenticationResponse;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public AuthenticationResponse signIn(@RequestBody AuthenticationRequest request) {
        log.info("SignIn is successfully");
        String token =  authenticationService.signIn(request);
        return new AuthenticationResponse(token);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> signOut(@RequestHeader("auth-token") String authToken) {
        log.info("SignOut is successfully");
        authenticationService.signOut(authToken);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
