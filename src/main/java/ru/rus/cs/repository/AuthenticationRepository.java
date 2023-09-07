package ru.rus.cs.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AuthenticationRepository {
    private final Map<String, String> tokens = new ConcurrentHashMap<>();

    public void putTokenAndUsername(String token, String username) {
        tokens.put(token, username);
    }

    public String getUserNameByToken(String token) {
        return tokens.get(token);
    }

    public void removeTokenAndUsernameByToken(String token) {
        tokens.remove(token);
    }

    public String getTokenByUsername(String username) {
        final var entrySet = tokens.entrySet();

        var token = "";
        for (final var pair : entrySet) {
            if (username.equals(pair.getValue())) token = pair.getKey();
        }

        if (token.equals("")) return null;

        return token;
    }
}
