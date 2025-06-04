package ru.yandex.transfer_service.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.transfer_service.model.TransactionType;

import java.util.Arrays;

public class TransactionTypeValidator implements ConstraintValidator<ValidTransactionType, String> {
    @Override
    public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(TransactionType.values())
                .map(Enum::toString)
                .map(String::toLowerCase)
                .anyMatch(currency -> currency.equals(input));
    }
}
