package ru.practicum.shareit.exception;

import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionRespons validation(final ValidationException ex) {
        return new ExceptionRespons(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionRespons notFound(final NotFoundException ex) {
        return new ExceptionRespons(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus
    public ExceptionRespons runTime(final RuntimeException ex) {
        return new ExceptionRespons(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionRespons valid(final UnexpectedTypeException ex) {
        return new ExceptionRespons(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionRespons dateException(final DateException ex) {
        return new ExceptionRespons(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionRespons ownerException(final OwnerException ex) {
        return new ExceptionRespons(ex.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionRespons commentException(final CommentException ex) {
        return new ExceptionRespons(ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionRespons availableException(final AvailableException ex) {
        return new ExceptionRespons(ex.getMessage());
    }

}

