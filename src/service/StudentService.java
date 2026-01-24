package service;

import model.Student;
import repository.StudentRepository;
import repository.HouseRepository;
import exception.*;

import java.util.List;
public class StudentService {
    private final StudentRepository studentRepository;
    private final HouseRepository houseRepository;

    public StudentService() {
        this.studentRepository = new StudentRepository();
        this.houseRepository = new HouseRepository();
    }

    public Student createStudent(Student student) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        student.validate();

        if (student.getHouseId() != null) {
            houseRepository.getById(student.getHouseId());
        }

        return studentRepository.create(student);
    }

    public List<Student> getAllStudents() throws DatabaseOperationException {
        return studentRepository.getAll();
    }

    public Student getStudentById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        return studentRepository.getById(id);
    }

    public Student updateStudent(int id, Student student) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        student.validate();

        if (student.getHouseId() != null) {
            houseRepository.getById(student.getHouseId());
        }

        return studentRepository.update(id, student);
    }

    public void deleteStudent(int id) throws DatabaseOperationException, ResourceNotFoundException {
        studentRepository.delete(id);
    }

    public List<Student> getStudentsByHouse(int houseId) throws DatabaseOperationException, ResourceNotFoundException {
        houseRepository.getById(houseId);
        return studentRepository.getByHouseId(houseId);
    }

    public void awardPointsToStudent(int studentId, int points) throws DatabaseOperationException, ResourceNotFoundException, InvalidInputException {
        if (points <= 0) {
            throw new InvalidInputException("Points to award must be positive");
        }

        Student student = studentRepository.getById(studentId);
        student.addPoints(points);

        System.out.println("Awarded " + points + " points to " + student.getName());
    }
}