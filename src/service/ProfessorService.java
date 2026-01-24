package service;

import model.Professor;
import repository.ProfessorRepository;
import repository.HouseRepository;
import exception.*;

import java.util.List;


public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final HouseRepository houseRepository;

    public ProfessorService() {
        this.professorRepository = new ProfessorRepository();
        this.houseRepository = new HouseRepository();
    }

    public Professor createProfessor(Professor professor) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        professor.validate();

        if (professor.getHouseId() != null) {
            houseRepository.getById(professor.getHouseId());
        }

        return professorRepository.create(professor);
    }

    public List<Professor> getAllProfessors() throws DatabaseOperationException {
        return professorRepository.getAll();
    }

    public Professor getProfessorById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        return professorRepository.getById(id);
    }

    public Professor updateProfessor(int id, Professor professor) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        professor.validate();

        if (professor.getHouseId() != null) {
            houseRepository.getById(professor.getHouseId());
        }

        return professorRepository.update(id, professor);
    }

    public void deleteProfessor(int id) throws DatabaseOperationException, ResourceNotFoundException {
        professorRepository.delete(id);
    }

    public void giveRaise(int professorId, double amount) throws DatabaseOperationException, ResourceNotFoundException, InvalidInputException {
        if (amount <= 0) {
            throw new InvalidInputException("Raise amount must be positive");
        }

        Professor professor = professorRepository.getById(professorId);
        double newSalary = professor.getSalary() + amount;
        professor.setSalary(newSalary);

        professorRepository.update(professorId, professor);

        System.out.println("Professor " + professor.getName() + " received a raise. New salary: " + newSalary);
    }
}