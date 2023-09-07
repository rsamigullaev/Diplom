package ru.rus.cs.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationResponse(
        @JsonProperty("auth-token")
        String authToken
) {
}
