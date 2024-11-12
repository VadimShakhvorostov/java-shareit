package ru.practicum.shareit.exception;

import lombok.Getter;

@Getter
public class ExceptionRespons {
    private final String error;

    public ExceptionRespons(String error) {
        this.error = error;
    }
}
