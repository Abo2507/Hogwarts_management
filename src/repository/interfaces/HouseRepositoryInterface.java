package repository.interfaces;

import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;
import model.House;

public interface HouseRepositoryInterface extends CrudRepository<House> {
    void updatePoints(int id, int points) throws DatabaseOperationException, ResourceNotFoundException;
}
