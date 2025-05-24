package service;

import entity.Priority;
import entity.Todo;
import repository.InMemoryTodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import exception.TodoNotFoundException;
 
import static org.junit.jupiter.api.Assertions.*;

public class TodoServiceImplTest {

	private TodoServiceImpl service;

	@BeforeEach
	public void setup() {
		service = new TodoServiceImpl(new InMemoryTodoRepository());
	}

	@Test
	public void testAddTodo() {
		service.addTodo("Task 1", "Desc", Priority.HIGH, LocalDate.now());
		List<Todo> all = service.getAllTodos();
		assertEquals(1, all.size());
		assertEquals("Task 1", all.get(0).getTitle());
	}

	@Test
	public void testUpdateTodo() {
		service.addTodo("Old", "Old", Priority.LOW, LocalDate.now());
		Todo todo = service.getAllTodos().get(0);

		service.updateTodo(todo.getId(), "New Title", "New Desc", Priority.HIGH, LocalDate.now().plusDays(1));
		Todo updated = service.getAllTodos().get(0);

		assertEquals("New Title", updated.getTitle());
		assertEquals(Priority.HIGH, updated.getPriority());
	}

	@Test
	public void testDeleteTodo() {
		service.addTodo("To Delete", "desc", Priority.MEDIUM, LocalDate.now());
		Todo todo = service.getAllTodos().get(0);
		service.deleteTodo(todo.getId());
		assertTrue(service.getAllTodos().isEmpty());
	}

	@Test
	public void testSearchByKeyword() {
		service.addTodo("Project Work", "Java", Priority.MEDIUM, LocalDate.now());
		List<Todo> results = service.searchByKeyword("project");
		assertEquals(1, results.size());
	}

	@Test
	public void testMarkComplete() {
		service.addTodo("Complete Me", "test", Priority.LOW, LocalDate.now());
		Todo todo = service.getAllTodos().get(0);
		service.markComplete(todo.getId(), true);
		assertTrue(service.getAllTodos().get(0).isCompleted());
	}

	void testGetTodoByInvalidIdThrowsException() {
		Exception exception = assertThrows(RuntimeException.class, () -> {
			service.getTodoById("invalid-id");
		});
		assertTrue(exception.getMessage().toLowerCase().contains("not found"));
	}

	@Test  
	void testDeleteTodoByInvalidIdThrowsException() {
		Exception exception = assertThrows(RuntimeException.class, () -> {
			service.deleteTodo("invalid-id");
		});
		assertTrue(exception.getMessage().toLowerCase().contains("not found"));
	}

	void testDeleteTodoThrowsTodoNotFoundException() {
		assertThrows(TodoNotFoundException.class, () -> {
			service.deleteTodo("invalid-id");
		});
	}
}