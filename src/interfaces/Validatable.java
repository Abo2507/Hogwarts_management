package interfaces;

import exception.InvalidInputException;

/**
 * Interface for entities that can be validated
 * Demonstrates: Interface design and polymorphism
 */
public interface Validatable {
    /**
     * Validates the entity's data
     * @throws InvalidInputException if validation fails
     */
    void validate() throws InvalidInputException;
}