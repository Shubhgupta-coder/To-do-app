package util;

import entity.Priority;

import entity.Todo;

import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Test;

import java.io.*;

import java.time.LocalDate;

import java.util.ArrayList;

import java.util.Collections;

import java.util.List;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TodoFileHandlerTest {

	private final String filePath = "todos.csv";

	@AfterEach

	void cleanup() {

		File file = new File(filePath);

		if (file.exists()) {

			file.delete();

		}

	}

	private Todo createTodo(String title, String desc, Priority priority, LocalDate due, boolean completed) {

		return new Todo(

				UUID.randomUUID().toString(),

				title,

				desc,

				priority,

				due,

				LocalDate.now(),

				completed

		);

	}

	@Test

	void testSaveAndLoadTodos() throws ReflectiveOperationException {

		List<Todo> original = new ArrayList<>();

		Todo todo = createTodo("Sample Task", "Test description", Priority.HIGH, LocalDate.now().plusDays(2), false);

		original.add(todo);

		FileHandler.saveTodos(original);

		List<Todo> loaded = FileHandler.loadTodos();

		assertEquals(1, loaded.size());

		Todo loadedTodo = loaded.get(0);

		assertEquals(todo.getTitle(), loadedTodo.getTitle());

		assertEquals(todo.getDescription(), loadedTodo.getDescription());

		assertEquals(todo.getPriority(), loadedTodo.getPriority());

		assertEquals(todo.getDueDate(), loadedTodo.getDueDate());

		assertEquals(todo.isCompleted(), loadedTodo.isCompleted());

	}

	@Test

	void testSaveAndLoadCompletedTodo() throws IOException, ReflectiveOperationException {

// Use the helper method to create a completed todo

		Todo todo = createTodo("Test Complete", "Done", Priority.HIGH, LocalDate.now().plusDays(1), true);

// Save to file using Collections.singletonList() for a single element list, compatible with older Java versions

		FileHandler.saveTodos(Collections.singletonList(todo));

		List<Todo> loaded = FileHandler.loadTodos();

// Verify that the todo was loaded and its completed status is true, and title matches

		assertEquals(1, loaded.size());

		assertTrue(loaded.get(0).isCompleted());

		assertEquals("Test Complete", loaded.get(0).getTitle()); // Re-added this assertion

	}

	@Test

	void testSaveAndLoadWithCommas() throws ReflectiveOperationException {

		Todo todo = createTodo("Title, with comma", "Description, also comma", Priority.LOW,
				LocalDate.now().plusDays(2), false);

		List<Todo> list = new ArrayList<>();

		list.add(todo);

		FileHandler.saveTodos(list);

		List<Todo> loaded = FileHandler.loadTodos();

		assertEquals("Title, with comma", loaded.get(0).getTitle());

		assertEquals("Description, also comma", loaded.get(0).getDescription());

	}

	@Test

	void testLoadFromMissingFile() throws ReflectiveOperationException {

		new File(filePath).delete();

		List<Todo> loaded = FileHandler.loadTodos();

		assertNotNull(loaded);

		assertTrue(loaded.isEmpty());

	}

	@Test

	void testLoadMalformedLineGracefully() throws IOException, ReflectiveOperationException {

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

			writer.write("not,a,valid,line\n");

		}

		List<Todo> loaded = FileHandler.loadTodos();

		assertTrue(loaded.isEmpty());

	}

	@Test

	void testSaveEmptyList() throws ReflectiveOperationException {

		FileHandler.saveTodos(new ArrayList<>());

		List<Todo> loaded = FileHandler.loadTodos();

		assertTrue(loaded.isEmpty());

	}

	@Test

	void testSpecialCharactersEscaping() throws IOException, ReflectiveOperationException { // Changed to IOException

// Create a Todo with a title containing double quotes and a description with a newline

		Todo todo = createTodo("Title \"quoted\"", "Desc\nnewline", Priority.MEDIUM, LocalDate.now().plusDays(1), true);

// Save the single todo to the file

		FileHandler.saveTodos(Collections.singletonList(todo)); // Using Collections.singletonList for consistency

// Load the todos from the file

		List<Todo> loaded = FileHandler.loadTodos();

// Verify that one todo was loaded

		assertEquals(1, loaded.size());

// Verify the title matches, including the escaped double quotes

		assertEquals(todo.getTitle(), loaded.get(0).getTitle());

// Verify the description matches, handling potential platform-specific newline differences
 
		assertEquals(todo.getDescription().replaceAll("\r\n?", "\n"),
				loaded.get(0).getDescription().replaceAll("\r\n?", "\n"));

	}

}