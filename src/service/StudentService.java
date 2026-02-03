package service;

import model.House;
import model.Student;
import repository.interfaces.CrudRepository;
import repository.interfaces.StudentRepositoryInterface;
import service.interfaces.IStudentService;
import exception.*;
import java.util.List;

public class StudentService implements IStudentService {

    private final StudentRepositoryInterface studentRepository;
    private final CrudRepository<House> houseRepository;

    public StudentService(StudentRepositoryInterface studentRepository, CrudRepository<House> houseRepository) {
        this.studentRepository = studentRepository;
        this.houseRepository = houseRepository;
    }

    @Override
    public Student createStudent(Student student) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        student.validate();

        if (student.getHouseId() != null) {
            houseRepository.getById(student.getHouseId());
        }

        return studentRepository.create(student);
    }

    @Override
    public List<Student> getAllStudents() throws DatabaseOperationException {
        return studentRepository.getAll();
    }

    @Override
    public Student getStudentById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        return studentRepository.getById(id);
    }

    @Override
    public Student updateStudent(int id, Student student) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        student.validate();

        if (student.getHouseId() != null) {
            houseRepository.getById(student.getHouseId());
        }

        return studentRepository.update(id, student);
    }

    @Override
    public void deleteStudent(int id) throws DatabaseOperationException, ResourceNotFoundException {
        studentRepository.delete(id);
    }

    @Override
    public List<Student> getStudentsByHouse(int houseId) throws DatabaseOperationException, ResourceNotFoundException {
        houseRepository.getById(houseId);

        return studentRepository.getByHouseId(houseId);
    }
}
