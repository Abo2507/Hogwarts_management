package interfaces;

import exception.InvalidInputException;

public interface Validatable {

    void validate() throws InvalidInputException;

    default boolean isValid() {
        try {
            validate();
            return true;
        } catch (InvalidInputException e) {
            return false;
        }
    }

    static void validateNotEmpty(String value, String fieldName) throws InvalidInputException {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidInputException(fieldName + " cannot be empty");
        }
    }
    static void validatePositive(int value, String fieldName) throws InvalidInputException {
        if (value <= 0) {
            throw new InvalidInputException(fieldName + " must be positive");
        }
    }
}