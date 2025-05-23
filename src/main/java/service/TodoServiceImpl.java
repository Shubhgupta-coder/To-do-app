package service;

import entity.Priority;
import entity.Todo;
import exception.TodoNotFoundException;
import repository.TodoRepository;
import util.ValidationUtil;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TodoServiceImpl implements TodoService {


private final TodoRepository repository;

public TodoServiceImpl(TodoRepository repository) {
    this.repository = repository;
}

@Override
public void addTodo(String title, String description, Priority priority, LocalDate dueDate) {
ValidationUtil.validateTitle(title);
ValidationUtil.validateDescription(description);
ValidationUtil.validatePriority(priority);
ValidationUtil.validateDueDate(dueDate);


Todo todo = new Todo(title, description, priority, dueDate);
repository.save(todo);
}

@Override
public void loadExistingTodos(List<Todo> todos) {
    for (Todo todo : todos) {
        repository.save(todo);
    }
}

@Override
public List<Todo> getAllTodos() {
    return repository.findAll();
}

@Override
public Todo getTodoById(String id) {
    return repository.findById(id)
            .orElseThrow(() -> new TodoNotFoundException("Todo with ID " + id + " not found"));
}

@Override
public void updateTodo(String id, String title, String description, Priority priority, LocalDate dueDate) {
    Todo todo = getTodoById(id);
    if (title != null) todo.setTitle(title);
    if (description != null) todo.setDescription(description);
    if (priority != null) todo.setPriority(priority);
    if (dueDate != null) todo.setDueDate(dueDate);
    repository.update(todo);
}

@Override
public void deleteTodo(String id) {
    // Check if the Optional is NOT present (i.e., it's empty)
    if (!repository.findById(id).isPresent()) {
        throw new TodoNotFoundException("Todo with ID " + id + " not found");
    }
    repository.delete(id);
}

@Override
public void markComplete(String id, boolean completed) {
    Todo todo = getTodoById(id);
    todo.setCompleted(completed);
    repository.update(todo);
}

@Override
public List<Todo> searchByKeyword(String keyword) {
    return repository.findAll().stream()
            .filter(todo ->
                    todo.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    todo.getDescription().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
}

@Override
public List<Todo> filterByPriority(Priority priority) {
    return repository.findAll().stream()
            .filter(todo -> todo.getPriority() == priority)
            .collect(Collectors.toList());
}

@Override
public List<Todo> filterByCompletionStatus(boolean completed) {
    return repository.findAll().stream()
            .filter(todo -> todo.isCompleted() == completed)
            .collect(Collectors.toList());
}

@Override
public List<Todo> filterByDueDateRange(LocalDate from, LocalDate to) {
    return repository.findAll().stream()
            .filter(todo -> {
                LocalDate due = todo.getDueDate();
                return (due.isAfter(from.minusDays(1)) && due.isBefore(to.plusDays(1)));
            })
            .collect(Collectors.toList());
}

@Override
public List<Todo> sortByPriority() {
    return repository.findAll().stream()
            .sorted(Comparator.comparing(todo -> todo.getPriority().ordinal()))
            .collect(Collectors.toList());
}

@Override
public List<Todo> sortByDueDate() {
    return repository.findAll().stream()
            .sorted(Comparator.comparing(Todo::getDueDate))
            .collect(Collectors.toList());
}

@Override
public List<Todo> sortByCreatedDate() {
    return repository.findAll().stream()
            .sorted(Comparator.comparing(Todo::getCreatedDate))
            .collect(Collectors.toList());
}
}