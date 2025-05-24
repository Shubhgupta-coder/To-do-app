//package repository;
//
//import entity.Todo;
//import java.util.*;
//
//public class InMemoryTodoRepository implements TodoRepository {
//private final Map<String, Todo> todoMap = new HashMap<>();
//
//@Override 
//public void save(Todo todo) {
//    todoMap.put(todo.getId(), todo);
//}
//
//@Override
//public Optional<Todo> findById(String id) {
//    return Optional.ofNullable(todoMap.get(id));
//}
//
//@Override
//public List<Todo> findAll() {
//    return new ArrayList<>(todoMap.values());
//}
//
//@Override
//public void update(Todo todo) {
//    todoMap.put(todo.getId(), todo); // Same key replaces existing
//}
//
//@Override
//public void delete(String id) {
//    todoMap.remove(id);
//}
//
//
//}


package repository;

import entity.Todo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap; // Use ConcurrentHashMap for thread safety if needed, or HashMap for simplicity

public class InMemoryTodoRepository implements TodoRepository {

    // Using a Map to store todos by ID for efficient lookup and update
    private final Map<String, Todo> todos = new ConcurrentHashMap<>();

    @Override
    public void save(Todo todo) {
        // In a real scenario, you might check if ID already exists and throw error.
        // For in-memory, just put it. If ID is null, generate one (though Todo constructor handles this).
        if (todo.getId() == null || todo.getId().isEmpty()) {
            // This case should ideally not happen if Todo constructor generates ID
            throw new IllegalArgumentException("Todo ID cannot be null or empty when saving.");
        }
        todos.put(todo.getId(), todo);
    }

    @Override
    public void update(Todo todo) {
        if (todo.getId() == null || !todos.containsKey(todo.getId())) {
            // In a real scenario, you might throw a specific "NotFound" exception
            throw new IllegalArgumentException("Todo with ID " + todo.getId() + " not found for update.");
        }
        todos.put(todo.getId(), todo); // Overwrite the existing todo
    }

    @Override
    public void delete(String id) {
        todos.remove(id); // remove returns the removed value, or null if not found
        // The service layer is responsible for throwing TodoNotFoundException if not found
    }

    @Override
    public Optional<Todo> findById(String id) {
        return Optional.ofNullable(todos.get(id));
    }

    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(todos.values()); // Return a copy to prevent external modification
    }

    
    public void clear() {
        todos.clear(); // Implementation of the clear method
    }
}
