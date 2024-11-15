package ru.practicum.shareit.exception;

import jakarta.validation.UnexpectedTypeException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ErrorHandlerTest {

    private final ErrorHandler errorHandler;

    @Test
    public void availableExceptionTest() {
        ExceptionRespons exceptionRespons = errorHandler.availableException(new AvailableException("message"));
        assertEquals(exceptionRespons.getError(), "message");
    }

    @Test
    public void validationExceptionTest() {
        ExceptionRespons exceptionRespons = errorHandler.validation(new ValidationException("message"));
        assertEquals(exceptionRespons.getError(), "message");
    }

    @Test
    public void notFoundExceptionTest() {
        ExceptionRespons exceptionRespons = errorHandler.notFound(new NotFoundException("message"));
        assertEquals(exceptionRespons.getError(), "message");
    }

    @Test
    public void runTimeExceptionTest() {
        ExceptionRespons exceptionRespons = errorHandler.runTime(new RuntimeException("message"));
        assertEquals(exceptionRespons.getError(), "message");
    }

    @Test
    public void validExceptionTest() {
        ExceptionRespons exceptionRespons = errorHandler.valid(new UnexpectedTypeException("message"));
        assertEquals(exceptionRespons.getError(), "message");
    }

    @Test
    public void dateExceptionTest() {
        ExceptionRespons exceptionRespons = errorHandler.dateException(new DateException("message"));
        assertEquals(exceptionRespons.getError(), "message");
    }

    @Test
    public void ownerExceptionTest() {
        ExceptionRespons exceptionRespons = errorHandler.ownerException(new OwnerException("message"));
        assertEquals(exceptionRespons.getError(), "message");
    }

    @Test
    public void commentExceptionTest() {
        ExceptionRespons exceptionRespons = errorHandler.commentException(new CommentException("message"));
        assertEquals(exceptionRespons.getError(), "message");
    }

}
