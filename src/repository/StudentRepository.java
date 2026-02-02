package repository;

import model.Student;
import repository.interfaces.CrudRepository;
import util.DatabaseConnection;
import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements CrudRepository<Student> {

    @Override
    public Student create(Student student) throws DatabaseOperationException {
        String sql = "INSERT INTO persons (name, age, person_type, house_id, year, patronus) VALUES (?, ?, 'STUDENT', ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getName());
            stmt.setInt(2, student.getAge());
            stmt.setInt(3, student.getHouseId());
            stmt.setInt(4, student.getYear());
            stmt.setString(5, student.getPatronus());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    student.setId(keys.getInt(1));
                }
            }

            return student;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating student: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Student> getAll() throws DatabaseOperationException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM persons WHERE person_type = 'STUDENT'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }

            return students;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving students: " + e.getMessage(), e);
        }
    }

    @Override
    public Student getById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "SELECT * FROM persons WHERE id = ? AND person_type = 'STUDENT'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToStudent(rs);
                } else {
                    throw new ResourceNotFoundException("Student with ID " + id + " not found");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving student: " + e.getMessage(), e);
        }
    }

    @Override
    public Student update(int id, Student student) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "UPDATE persons SET name = ?, age = ?, house_id = ?, year = ?, patronus = ? WHERE id = ? AND person_type = 'STUDENT'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getName());
            stmt.setInt(2, student.getAge());
            stmt.setInt(3, student.getHouseId());
            stmt.setInt(4, student.getYear());
            stmt.setString(5, student.getPatronus());
            stmt.setInt(6, id);

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new ResourceNotFoundException("Student with ID " + id + " not found");
            }

            student.setId(id);
            return student;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating student: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "DELETE FROM persons WHERE id = ? AND person_type = 'STUDENT'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new ResourceNotFoundException("Student with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting student: " + e.getMessage(), e);
        }
    }

    public List<Student> getByHouseId(int houseId) throws DatabaseOperationException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM persons WHERE house_id = ? AND person_type = 'STUDENT'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, houseId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
            }

            return students;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving students by house: " + e.getMessage(), e);
        }
    }

    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getInt("house_id"),
                rs.getInt("year"),
                rs.getString("patronus")
        );
    }
}