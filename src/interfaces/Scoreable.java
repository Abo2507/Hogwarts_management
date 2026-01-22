package interfaces;

/**
 * Interface for entities that can have points
 * Demonstrates: Interface design and polymorphism
 */
public interface Scoreable {
    /**
     * Adds points to the entity
     * @param points number of points to add
     */
    void addPoints(int points);

    /**
     * Deducts points from the entity
     * @param points number of points to deduct
     */
    void deductPoints(int points);

    /**
     * Gets current points
     * @return current point total
     */
    int getPoints();
}