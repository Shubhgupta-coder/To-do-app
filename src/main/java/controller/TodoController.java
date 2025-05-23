package controller;

import entity.Priority;
import  entity.Todo;
import repository.InMemoryTodoRepository;
import service.TodoService;
import service.TodoServiceImpl;
import util.FileHandler;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import exception.TodoNotFoundException;
public class TodoController {


public final TodoService todoService;
private final Scanner scanner;

public TodoController() {
    this.todoService = new TodoServiceImpl(new InMemoryTodoRepository());
    this.scanner = new Scanner(System.in);
}

public void run() {
    boolean running = true;
    while (running) {
        showMenu();
        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                addTodo();
                break;
            case "2":
                listTodos();
                break;
            case "3":
                updateTodo();
                break;
            case "4":
                deleteTodo();
                break;
            case "5":
                markComplete();
                break;
            case "6":
                searchTodos();
                break;
            case "7":
                filterMenu();
                break;
            case "8":
                sortMenu();
                break;
            case "0":
                running = false;
                break;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }
}

private void showMenu() {
    System.out.println("\n== TODO APP MENU ==");
    System.out.println("1. Add Todo");
    System.out.println("2. View All Todos");
    System.out.println("3. Edit Todo");
    System.out.println("4. Delete Todo");
    System.out.println("5. Mark Complete/Incomplete");
    System.out.println("6. Search Todos");
    System.out.println("7. Filter Todos");
    System.out.println("8. Sort Todos");
    System.out.println("0. Exit");
    System.out.print("Enter choice: ");
}

private void addTodo() {
    try {
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        Priority priority = readPriority();
        System.out.print("Due Date (yyyy-mm-dd): ");
        LocalDate dueDate = LocalDate.parse(scanner.nextLine());
        todoService.addTodo(title, description, priority, dueDate);
        System.out.println("Todo added successfully.");
    } catch (Exception e) {
        System.out.println("Failed to add todo: " + e.getMessage());
    }
}

private void listTodos() {
    List<Todo> todos = todoService.getAllTodos();
    if (todos.isEmpty()) {
        System.out.println("No todos found.");
    } else {
        todos.forEach(System.out::println);
    }
}

private void updateTodo() {
    try {
        System.out.print("Enter Todo ID to update: ");
        String id = scanner.nextLine();
        System.out.print("New Title (leave blank to skip): ");
        String title = scanner.nextLine();
        System.out.print("New Description (leave blank to skip): ");
        String desc = scanner.nextLine();
        Priority priority = readPriorityOptional();
        System.out.print("New Due Date (yyyy-mm-dd) (leave blank to skip): ");
        String dueInput = scanner.nextLine();
        LocalDate dueDate = dueInput.isEmpty() ? null : LocalDate.parse(dueInput);

        todoService.updateTodo(
                id,
                title.isEmpty() ? null : title,
                desc.isEmpty() ? null : desc,
                priority,
                dueDate
        );
        System.out.println("Todo updated.");
    } catch (TodoNotFoundException e) {
    	System.out.println("Update failed: " + e.getMessage());
    	} catch (Exception e) {
    	System.out.println("Invalid input: " + e.getMessage());
    	}
}

private void deleteTodo() {
try {
System.out.print("Enter Todo ID to delete: ");
String id = scanner.nextLine();
todoService.deleteTodo(id);
System.out.println("Todo deleted.");
} catch (TodoNotFoundException e) {
System.out.println(e.getMessage());
}
}

private void markComplete() {
try {
System.out.print("Enter Todo ID: ");
String id = scanner.nextLine();
System.out.print("Mark as complete? (y/n): ");
String input = scanner.nextLine().trim().toLowerCase();
todoService.markComplete(id, input.equals("y"));
System.out.println("Status updated.");
} catch (TodoNotFoundException e) {
System.out.println(e.getMessage());
}
}
private void searchTodos() {
    System.out.print("Enter keyword: ");
    String keyword = scanner.nextLine();
    List<Todo> results = todoService.searchByKeyword(keyword);
    if (results.isEmpty()) {
        System.out.println("No matching todos.");
    } else {
        results.forEach(System.out::println);
    }
}

private void filterMenu() {
    System.out.println("Filter by:");
    System.out.println("1. Priority");
    System.out.println("2. Completion Status");
    System.out.println("3. Due Date Range");
    System.out.print("Choice: ");
    String choice = scanner.nextLine();
    switch (choice) {
        case "1":
            Priority priority = readPriority();
            todoService.filterByPriority(priority).forEach(System.out::println);
            break;
        case "2":
            System.out.print("Show completed? (y/n): ");
            boolean completed = scanner.nextLine().trim().equalsIgnoreCase("y");
            todoService.filterByCompletionStatus(completed).forEach(System.out::println);
            break;
        case "3":
            try {
                System.out.print("From date (yyyy-mm-dd): ");
                LocalDate from = LocalDate.parse(scanner.nextLine());
                System.out.print("To date (yyyy-mm-dd): ");
                LocalDate to = LocalDate.parse(scanner.nextLine());
                todoService.filterByDueDateRange(from, to).forEach(System.out::println);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format.");
            }
            break;
        default:
            System.out.println("Invalid filter option.");
    }
}

private void sortMenu() {
    System.out.println("Sort by:");
    System.out.println("1. Priority");
    System.out.println("2. Due Date");
    System.out.println("3. Created Date");
    System.out.print("Choice: ");
    String choice = scanner.nextLine();
    switch (choice) {
        case "1":
            todoService.sortByPriority().forEach(System.out::println);
            break;
        case "2":
            todoService.sortByDueDate().forEach(System.out::println);
            break;
        case "3":
            todoService.sortByCreatedDate().forEach(System.out::println);
            break;
        default:
            System.out.println("Invalid sort option.");
    }
}

private Priority readPriority() {
    while (true) {
        System.out.print("Priority (LOW/MEDIUM/HIGH): ");
        try {
            return Priority.valueOf(scanner.nextLine().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid priority. Try again.");
        }
    }
}

private Priority readPriorityOptional() {
    System.out.print("Priority (LOW/MEDIUM/HIGH or blank to skip): ");
    String input = scanner.nextLine().trim();
    if (input.isEmpty()) return null;
    try {
        return Priority.valueOf(input.toUpperCase());
    } catch (IllegalArgumentException e) {
        System.out.println("Invalid priority entered. Skipping...");
        return null;
    }
}

// Entry point
public static void main(String[] args) throws ReflectiveOperationException {
    TodoController controller = new TodoController();

    // Load todos from file
    List<Todo> loaded = FileHandler.loadTodos();
    controller.todoService.loadExistingTodos(loaded);

    // Run app
    controller.run();

    // Save todos to file
    FileHandler.saveTodos(controller.todoService.getAllTodos());
}
}