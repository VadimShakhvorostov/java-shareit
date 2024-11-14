package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

public class DateExceptionTest {

    @Test
    void setDateException() {
        DateException dateException = new DateException("error");
    }
}
