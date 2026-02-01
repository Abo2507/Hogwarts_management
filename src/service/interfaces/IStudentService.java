package service.interfaces;

import model.Student;
import exception.*;

import java.util.List;

public interface IStudentService {

    Student createStudent(Student student) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException;

    List<Student> getAllStudents() throws DatabaseOperationException;

    Student getStudentById(int id) throws DatabaseOperationException, ResourceNotFoundException;

    Student updateStudent(int id, Student student) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException;

    void deleteStudent(int id) throws DatabaseOperationException, ResourceNotFoundException;

    List<Student> getStudentsByHouse(int houseId) throws DatabaseOperationException, ResourceNotFoundException;
}