package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

public class ValidationExceptionTest {

    @Test
    void setValidationException() {
        ValidationException validationException = new ValidationException("error");
    }
}
