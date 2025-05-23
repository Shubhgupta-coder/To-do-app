package repository;

import entity.Todo;
import java.util.*;

public class InMemoryTodoRepository implements TodoRepository {
private final Map<String, Todo> todoMap = new HashMap<>();

@Override
public void save(Todo todo) {
    todoMap.put(todo.getId(), todo);
}

@Override
public Optional<Todo> findById(String id) {
    return Optional.ofNullable(todoMap.get(id));
}

@Override
public List<Todo> findAll() {
    return new ArrayList<>(todoMap.values());
}

@Override
public void update(Todo todo) {
    todoMap.put(todo.getId(), todo); // Same key replaces existing
}

@Override
public void delete(String id) {
    todoMap.remove(id);
}


}