package ru.yandex.accounts_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.accounts_service.exception.NotEnoughMoneyException;
import ru.yandex.accounts_service.exception.NotFoundException;
import ru.yandex.accounts_service.exception.UserNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(Exception e, Model model) {
        return ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler({NotEnoughMoneyException.class})
    @ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
    public ErrorResponse handleNotEnoughMoneyException(Exception e, Model model) {
        return ErrorResponse.builder()
                .status(HttpStatus.PAYMENT_REQUIRED)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e, Model model) {
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage())
                .build();
    }
}
