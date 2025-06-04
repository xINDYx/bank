package ru.yandex.accounts_service.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long userId) {
        super(String.format("User with id = %d not found", userId));
    }

    public UserNotFoundException() {
        super("User with login and password does not found");
    }
}
