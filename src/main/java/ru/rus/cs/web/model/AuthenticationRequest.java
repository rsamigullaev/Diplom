package ru.rus.cs.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthenticationRequest(
        @NotNull(message = "'login' must not be null")
        @NotBlank(message = "'login' must not be blank")
        String login,
        @NotNull(message = "'password' must not be null")
        @NotBlank(message = "'password' must not be blank")
        @Size(min = 2, max = 30, message = "'password' should be between 2 and 30 characters")
        String password
) {
}