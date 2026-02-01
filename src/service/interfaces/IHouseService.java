package service.interfaces;

import model.House;
import exception.*;

import java.util.List;

public interface IHouseService {

    House createHouse(House house) throws InvalidInputException, DatabaseOperationException, DuplicateResourceException;
    List<House> getAllHouses() throws DatabaseOperationException;
    House getHouseById(int id) throws DatabaseOperationException, ResourceNotFoundException;

    House updateHouse(int id, House house) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException;
    void deleteHouse(int id) throws DatabaseOperationException, ResourceNotFoundException;
    void addPoints(int houseId, int points) throws DatabaseOperationException, ResourceNotFoundException, InvalidInputException;
    void deductPoints(int houseid, int points) throws DatabaseOperationException, ResourceNotFoundException, InvalidInputException;

}
