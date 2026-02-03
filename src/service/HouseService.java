package service;

import model.House;
import repository.interfaces.HouseRepositoryInterface;
import service.interfaces.IHouseService;
import exception.*;
import java.util.List;

public class HouseService implements IHouseService {

    private final HouseRepositoryInterface houseRepository;

    public HouseService(HouseRepositoryInterface houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Override
    public House createHouse(House house) throws InvalidInputException, DatabaseOperationException, DuplicateResourceException {
        validateHouse(house);

        List<House> existingHouses = houseRepository.getAll();
        for (House existing : existingHouses) {
            if (existing.getName().equalsIgnoreCase(house.getName())) {
                throw new DuplicateResourceException("House with name '" + house.getName() + "' already exists");
            }
        }

        return houseRepository.create(house);
    }

    @Override
    public List<House> getAllHouses() throws DatabaseOperationException {
        return houseRepository.getAll();
    }

    @Override
    public House getHouseById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        return houseRepository.getById(id);
    }

    @Override
    public House updateHouse(int id, House house) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        validateHouse(house);
        return houseRepository.update(id, house);
    }

    @Override
    public void deleteHouse(int id) throws DatabaseOperationException, ResourceNotFoundException {
        houseRepository.delete(id);
    }

    @Override
    public void addPoints(int houseId, int points) throws DatabaseOperationException, ResourceNotFoundException, InvalidInputException {
        if (points <= 0) {
            throw new InvalidInputException("Points to add must be positive");
        }

        House house = houseRepository.getById(houseId);
        house.addPoints(points);

        houseRepository.updatePoints(houseId, house.getPoints());
    }

    @Override
    public void deductPoints(int houseId, int points) throws DatabaseOperationException, ResourceNotFoundException, InvalidInputException {
        if (points <= 0) {
            throw new InvalidInputException("Points to deduct must be positive");
        }

        House house = houseRepository.getById(houseId);
        house.deductPoints(points);


        houseRepository.updatePoints(houseId, house.getPoints());
    }

    private void validateHouse(House house) throws InvalidInputException {
        if (house.getName() == null || house.getName().trim().isEmpty()) {
            throw new InvalidInputException("House name cannot be empty");
        }
        if (house.getFounder() == null || house.getFounder().trim().isEmpty()) {
            throw new InvalidInputException("House founder cannot be empty");
        }
        if (house.getPoints() < 0) {
            throw new InvalidInputException("House points cannot be negative");
        }
    }
}
