package repository;

import model.Professor;
import repository.interfaces.CrudRepository;
import util.DatabaseConnection;
import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfessorRepository implements CrudRepository<Professor> {

    @Override
    public Professor create(Professor professor) throws DatabaseOperationException {
        String sql = "INSERT INTO persons (name, age, person_type, house_id, subject, salary) VALUES (?, ?, 'PROFESSOR', ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, professor.getName());
            stmt.setInt(2, professor.getAge());

            if (professor.getHouseId() != null) {
                stmt.setInt(3, professor.getHouseId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, professor.getSubject());
            stmt.setDouble(5, professor.getSalary());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    professor.setId(keys.getInt(1));
                }
            }

            return professor;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating professor: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Professor> getAll() throws DatabaseOperationException {
        List<Professor> professors = new ArrayList<>();
        String sql = "SELECT * FROM persons WHERE person_type = 'PROFESSOR'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                professors.add(mapResultSetToProfessor(rs));
            }

            return professors;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving professors: " + e.getMessage(), e);
        }
    }

    @Override
    public Professor getById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "SELECT * FROM persons WHERE id = ? AND person_type = 'PROFESSOR'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProfessor(rs);
                } else {
                    throw new ResourceNotFoundException("Professor with ID " + id + " not found");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving professor: " + e.getMessage(), e);
        }
    }

    @Override
    public Professor update(int id, Professor professor) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "UPDATE persons SET name = ?, age = ?, house_id = ?, subject = ?, salary = ? WHERE id = ? AND person_type = 'PROFESSOR'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, professor.getName());
            stmt.setInt(2, professor.getAge());

            if (professor.getHouseId() != null) {
                stmt.setInt(3, professor.getHouseId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.setString(4, professor.getSubject());
            stmt.setDouble(5, professor.getSalary());
            stmt.setInt(6, id);

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new ResourceNotFoundException("Professor with ID " + id + " not found");
            }

            professor.setId(id);
            return professor;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating professor: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "DELETE FROM persons WHERE id = ? AND person_type = 'PROFESSOR'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new ResourceNotFoundException("Professor with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting professor: " + e.getMessage(), e);
        }
    }

    private Professor mapResultSetToProfessor(ResultSet rs) throws SQLException {
        Integer houseId = rs.getInt("house_id");
        if (rs.wasNull()) {
            houseId = null;
        }

        return new Professor(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                houseId,
                rs.getString("subject"),
                rs.getDouble("salary")
        );
    }
}