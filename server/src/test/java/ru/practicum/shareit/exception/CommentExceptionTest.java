package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

public class CommentExceptionTest {

    @Test
    void setCommentExceptionTest() {
        CommentException commentException = new CommentException("error");
    }
}
