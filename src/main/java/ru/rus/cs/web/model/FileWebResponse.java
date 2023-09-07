package ru.rus.cs.web.model;

public record FileWebResponse(
        String filename,
        Integer size
) {
    public FileWebResponse() {
        this("", 0);
    }
}