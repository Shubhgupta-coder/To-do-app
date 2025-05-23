package util;

import entity.Priority;
import entity.Todo;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {


private static final String FILE_PATH = "todos.csv";

public static void saveTodos(List<Todo> todos) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
        for (Todo todo : todos) {
            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                    todo.getId(),
                    escapeCommas(todo.getTitle()),
                    escapeCommas(todo.getDescription()),
                    todo.getPriority(),
                    todo.getDueDate(),
                    todo.getCreatedDate(),
                    todo.isCompleted()));
        }
    } catch (IOException e) {
        System.out.println("Failed to save todos: " + e.getMessage());
    }
}

public static List<Todo> loadTodos() throws ReflectiveOperationException {
    List<Todo> todos = new ArrayList<>();
    File file = new File(FILE_PATH);
    if (!file.exists()) return todos;

    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = parseCsvLine(line);
            if (parts.length < 7) continue;

            Todo todo = new Todo(
            		parts[0], // id
            		parts[1], // title
            		parts[2], // description
            		Priority.valueOf(parts[3]), // priority
            		LocalDate.parse(parts[4]), // dueDate
            		LocalDate.parse(parts[5]), // createdDate
            		Boolean.parseBoolean(parts[6]) // completed
            		);



            // set ID, createdDate, and completed
//            setField(todo, "id", parts[0]);
//            setField(todo, "createdDate", LocalDate.parse(parts[5]));
//            todo.setCompleted(Boolean.parseBoolean(parts[6]));

            todos.add(todo);
        }
    } catch (IOException e) {
        System.out.println("Failed to load todos: " + e.getMessage());
    }

    return todos;
}

// Helpers

private static String escapeCommas(String input) {
    return input.replace(",", "%2C");
}

private static String[] parseCsvLine(String line) {
    return line.split("(?<!%)\\,", -1);
}

//private static void setField(Todo todo, String fieldName, Object value) throws ReflectiveOperationException {
//    var field = Todo.class.getDeclaredField(fieldName);
//    field.setAccessible(true);
//    field.set(todo, value);
//}
}