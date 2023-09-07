package ru.rus.cs.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FileNameEditRequest(
        @NotNull(message = "'filename' must not be null")
        @NotBlank(message = "'filename' must not be blank")
        String filename
) {
}
