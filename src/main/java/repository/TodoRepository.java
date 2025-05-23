package repository;

import entity.Todo;
import java.util.List;
import java.util.Optional;

public interface TodoRepository {
void save(Todo todo);
Optional<Todo> findById(String id);
List<Todo> findAll();
void update(Todo todo);
void delete(String id);
}