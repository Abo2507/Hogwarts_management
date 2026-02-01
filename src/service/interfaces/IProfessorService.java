package service.interfaces;

import model.Professor;
import exception.*;

import java.util.List;

public interface IProfessorService {
    Professor createProfessor(Professor professor) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException;
    List<Professor> getAllProfessors() throws DatabaseOperationException;
    Professor getProfessorById(int id) throws DatabaseOperationException, ResourceNotFoundException;

    Professor updateProfessor(int id, Professor professor) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException;
    void  deleteProfessor(int id) throws DatabaseOperationException, ResourceNotFoundException;
    void giveRaise(int professorId, double amount) throws DatabaseOperationException, ResourceNotFoundException, InvalidInputException;
}
