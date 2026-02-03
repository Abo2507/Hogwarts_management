package repository.interfaces;

import exception.DatabaseOperationException;
import model.Student;
import java.util.List;

public interface StudentRepositoryInterface extends CrudRepository<Student> {
    List<Student> getByHouseId(int houseId) throws DatabaseOperationException;
}
