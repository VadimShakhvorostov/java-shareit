package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

public class NotFoundExceptionTest {

    @Test
    void setNotFoundException() {
        NotFoundException notFoundException = new NotFoundException("error");
    }
}
