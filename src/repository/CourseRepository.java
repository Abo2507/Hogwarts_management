package repository;

import model.Course;
import util.DatabaseConnection;
import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepository {

    public Course create(Course course) throws DatabaseOperationException {
        String sql = "INSERT INTO courses (name, professor_id, credits) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, course.getName());

            if (course.getProfessorId() != null) {
                stmt.setInt(2, course.getProfessorId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setInt(3, course.getCredits());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    course.setId(keys.getInt(1));
                }
            }

            return course;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error creating course: " + e.getMessage(), e);
        }
    }

    public List<Course> getAll() throws DatabaseOperationException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }

            return courses;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving courses: " + e.getMessage(), e);
        }
    }

    public Course getById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "SELECT * FROM courses WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCourse(rs);
                } else {
                    throw new ResourceNotFoundException("Course with ID " + id + " not found");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving course: " + e.getMessage(), e);
        }
    }

    public Course update(int id, Course course) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "UPDATE courses SET name = ?, professor_id = ?, credits = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, course.getName());

            if (course.getProfessorId() != null) {
                stmt.setInt(2, course.getProfessorId());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }

            stmt.setInt(3, course.getCredits());
            stmt.setInt(4, id);

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new ResourceNotFoundException("Course with ID " + id + " not found");
            }

            course.setId(id);
            return course;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating course: " + e.getMessage(), e);
        }
    }

    public void delete(int id) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "DELETE FROM courses WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new ResourceNotFoundException("Course with ID " + id + " not found");
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting course: " + e.getMessage(), e);
        }
    }

    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Integer profId = rs.getInt("professor_id");
        if (rs.wasNull()) {
            profId = null;
        }

        return new Course(
                rs.getInt("id"),
                rs.getString("name"),
                profId,
                rs.getInt("credits")
        );
    }
}