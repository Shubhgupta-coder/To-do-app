package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class TodoControllerTest {

	private ByteArrayInputStream testIn;
	private final InputStream systemIn = System.in;

	@BeforeEach
	void restoreSystemIn() {
		System.setIn(systemIn); // Restore after each test
	}
	
	@Test
	void testAddTodoCommand() {
		String input = String.join(System.lineSeparator(), "1", // menu option: add
				"Test Title", // title
				"Test Description", // description
				"HIGH", // priority
				LocalDate.now().plusDays(2).toString(), // due date
				"0" // exit
		);
		testIn = new ByteArrayInputStream(input.getBytes());
		System.setIn(testIn);

		Scanner scanner = new Scanner(System.in);
		TodoController controller = new TodoController(scanner);

		 assertDoesNotThrow(controller::run); // Only run once, inside assertion
	}
	
	@Test
	void testInvalidMenuOption() {
	String input = String.join(System.lineSeparator(), "9", "0"); // invalid → exit
	ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
	System.setIn(testIn);

	
	Scanner scanner = new Scanner(System.in);
	TodoController controller = new TodoController(scanner);

	assertDoesNotThrow(controller::run);
	}
	
	@Test
	void testDeleteTodoCommand() {
	// Add → Delete → Exit
	String input = String.join(System.lineSeparator(),
	"1", "Delete Me", "Desc", "LOW", LocalDate.now().plusDays(1).toString(),
	"4", // delete
	"", // placeholder, will be replaced
	"0"
	);
	ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
	System.setIn(testIn);
	Scanner scanner = new Scanner(System.in);
	TodoController controller = new TodoController(scanner);

	controller.run();

	String id = controller.todoService.getAllTodos().get(0).getId();

	// Run delete
	String deleteInput = String.join(System.lineSeparator(), "4", id, "0");
	System.setIn(new ByteArrayInputStream(deleteInput.getBytes()));

	Scanner scanner2 = new Scanner(System.in);
	TodoController controller2 = new TodoController(scanner2);
	controller2.todoService.loadExistingTodos(controller.todoService.getAllTodos());

	assertDoesNotThrow(controller2::run);
	}
	
	
	@Test
	void testSearchCommand() {
	String input = String.join(System.lineSeparator(),
	"1", "Search Me", "Test Desc", "MEDIUM", LocalDate.now().plusDays(1).toString(),
	"6", "Search", 
	"0"
	);
	ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
	System.setIn(testIn);

	
	Scanner scanner = new Scanner(System.in);
	TodoController controller = new TodoController(scanner);

	assertDoesNotThrow(controller::run);
	}
	
	@Test
	void testMarkCompleteCommand() {
	String input = String.join(System.lineSeparator(),
	"1", "Finish Me", "Description", "HIGH", LocalDate.now().plusDays(2).toString(),
	"5", "", 
	"y",
	"0"
	);
	ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
	System.setIn(testIn);

	Scanner scanner = new Scanner(System.in);
	TodoController controller = new TodoController(scanner);

	controller.run();

	// Get the ID and re-run with correct input
 	String id = controller.todoService.getAllTodos().get(0).getId();
  
	String fullInput = String.join(System.lineSeparator(), "5", id, "y", "0");
	System.setIn(new ByteArrayInputStream(fullInput.getBytes()));
	Scanner scanner2 = new Scanner(System.in);
	TodoController controller2 = new TodoController(scanner2);
	 controller2.todoService.loadExistingTodos(controller.todoService.getAllTodos()); 
	assertDoesNotThrow(controller2::run);
	}
}