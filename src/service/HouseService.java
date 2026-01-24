package service;

import model.House;
import repository.HouseRepository;
import exception.*;

import java.util.List;


public class HouseService {
    private final HouseRepository houseRepository;

    public HouseService() {
        this.houseRepository = new HouseRepository();
    }

    public House createHouse(House house) throws InvalidInputException, DatabaseOperationException, DuplicateResourceException {
        // Validation
        validateHouse(house);

        // Check for duplicate house name
        try {
            List<House> existingHouses = houseRepository.getAll();
            for (House existing : existingHouses) {
                if (existing.getName().equalsIgnoreCase(house.getName())) {
                    throw new DuplicateResourceException("House with name '" + house.getName() + "' already exists");
                }
            }
        } catch (DatabaseOperationException e) {
            throw e;
        }

        return houseRepository.create(house);
    }

    public List<House> getAllHouses() throws DatabaseOperationException {
        return houseRepository.getAll();
    }

    public House getHouseById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        return houseRepository.getById(id);
    }

    public House updateHouse(int id, House house) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        validateHouse(house);
        return houseRepository.update(id, house);
    }

    public void deleteHouse(int id) throws DatabaseOperationException, ResourceNotFoundException {
        houseRepository.delete(id);
    }

    public void addPoints(int houseId, int points) throws DatabaseOperationException, ResourceNotFoundException, InvalidInputException {
        if (points <= 0) {
            throw new InvalidInputException("Points to add must be positive");
        }

        House house = houseRepository.getById(houseId);
        house.addPoints(points);
        houseRepository.updatePoints(houseId, house.getPoints());
    }

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