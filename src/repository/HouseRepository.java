package repository;

import model.House;
import repository.interfaces.HouseRepositoryInterface;
import util.DatabaseConnection;
import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HouseRepository implements HouseRepositoryInterface {

    @Override
    public House create(House house) throws DatabaseOperationException {
        String sql = "INSERT INTO houses (name, founder, points) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, house.getName());
            stmt.setString(2, house.getFounder());
            stmt.setInt(3, house.getPoints());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseOperationException("Creating house failed, no rows affected");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    house.setId(generatedKeys.getInt(1));
                } else {
                    throw new DatabaseOperationException("Creating house failed, no ID obtained");
                }
            }

            return house;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating house: " + e.getMessage(), e);
        }
    }

    @Override
    public List<House> getAll() throws DatabaseOperationException {
        List<House> houses = new ArrayList<>();
        String sql = "SELECT * FROM houses";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                House house = new House(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("founder"),
                        rs.getInt("points")
                );
                houses.add(house);
            }

            return houses;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving houses: " + e.getMessage(), e);
        }
    }

    @Override
    public House getById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "SELECT * FROM houses WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new House(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("founder"),
                            rs.getInt("points")
                    );
                } else {
                    throw new ResourceNotFoundException("House with ID " + id + " not found");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving house: " + e.getMessage(), e);
        }
    }

    @Override
    public House update(int id, House house) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "UPDATE houses SET name = ?, founder = ?, points = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, house.getName());
            stmt.setString(2, house.getFounder());
            stmt.setInt(3, house.getPoints());
            stmt.setInt(4, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ResourceNotFoundException("House with ID " + id + " not found");
            }

            house.setId(id);
            return house;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating house: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "DELETE FROM houses WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ResourceNotFoundException("House with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting house: " + e.getMessage(), e);
        }
    }

    @Override
    public void updatePoints(int id, int points) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "UPDATE houses SET points = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, points);
            stmt.setInt(2, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new ResourceNotFoundException("House with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating house points: " + e.getMessage(), e);
        }
    }
}