package service;

import entity.Priority;
import entity.Todo;

import java.time.LocalDate;
import java.util.List;

public interface TodoService {
void addTodo(String title, String description, Priority priority, LocalDate dueDate);
List<Todo> getAllTodos();
Todo getTodoById(String id);
void updateTodo(String id, String title, String description, Priority priority, LocalDate dueDate);
void deleteTodo(String id);
void markComplete(String id, boolean completed);
void loadExistingTodos(List<Todo> todos);

List<Todo> searchByKeyword(String keyword);
List<Todo> filterByPriority(Priority priority);
List<Todo> filterByCompletionStatus(boolean completed);
List<Todo> filterByDueDateRange(LocalDate from, LocalDate to);

List<Todo> sortByPriority();
List<Todo> sortByDueDate();
List<Todo> sortByCreatedDate();
}