package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

public class OwnerExceptionTest {

    @Test
    void setOwnerException() {
        OwnerException ownerException = new OwnerException("error");
    }
}
