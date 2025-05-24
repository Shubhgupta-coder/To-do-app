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

				String[] fields = {

						todo.getId(),

						escapeCsv(todo.getTitle()),

						escapeCsv(todo.getDescription()),

						todo.getPriority().name(),

						todo.getDueDate().toString(),

						todo.getCreatedDate().toString(),

						String.valueOf(todo.isCompleted())

				};

				writer.write(String.join(",", fields));

				writer.newLine();

			}

		} catch (IOException e) {

			System.out.println("Failed to save todos: " + e.getMessage());

		}

	}

	public static List<Todo> loadTodos() throws ReflectiveOperationException {

		List<Todo> todos = new ArrayList<>();

		File file = new File(FILE_PATH);

		if (!file.exists())
			return todos;

		try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {

			String line;

			while ((line = reader.readLine()) != null) {

				String[] parts = splitCsvLine(line);

				if (parts.length < 7)
					continue;

				Todo todo = new Todo(

						parts[0],

						parts[1],

						parts[2],

						Priority.valueOf(parts[3]),

						LocalDate.parse(parts[4]),

						LocalDate.parse(parts[5]),

						Boolean.parseBoolean(parts[6])

				);

				todos.add(todo);

			}

		} catch (IOException e) {

			System.out.println("Failed to load todos: " + e.getMessage());

		}

		return todos;

	}

	private static String escapeCsv(String field) {

		if (field.contains(",") || field.contains("\"") || field.contains("\n")) {

			field = field.replace("\"", "\"\"");

			return "\"" + field + "\"";

		}

		return field;

	}

	private static String[] splitCsvLine(String line) {

		List<String> tokens = new ArrayList<>();

		StringBuilder sb = new StringBuilder();

		boolean inQuotes = false;

		for (int i = 0; i < line.length(); i++) {

			char c = line.charAt(i);

			if (c == '\"') {

				inQuotes = !inQuotes;

			} else if (c == ',' && !inQuotes) {

				tokens.add(sb.toString().replace("\"\"", "\"")); // unescape

				sb.setLength(0);

			} else {

				sb.append(c);

			}

		}

		tokens.add(sb.toString().replace("\"\"", "\"")); // last field

		return tokens.toArray(new String[0]);

	}

}
