package repository.interfaces;

import exception.DatabaseOperationException;
import exception.ResourceNotFoundException;

import java.util.List;

public interface CrudRepository<T> {
    T create(T entity) throws DatabaseOperationException;

    List<T> getAll() throws DatabaseOperationException;

    T getById(int id) throws DatabaseOperationException, ResourceNotFoundException;

    T update(int id, T entity) throws DatabaseOperationException, ResourceNotFoundException;

    void delete(int id) throws DatabaseOperationException, ResourceNotFoundException;
}
