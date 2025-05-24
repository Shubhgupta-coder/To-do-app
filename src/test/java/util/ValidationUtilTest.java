package util;

import entity.Priority;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ValidationUtil class, ensuring all validation rules
 * are correctly applied and exceptions are thrown with expected messages.
 */
class ValidationUtilTest {
 
    @Test
    void testValidateTitle_valid() {
        // Test that a valid title does not throw an exception
        assertDoesNotThrow(() -> ValidationUtil.validateTitle("Valid Title"));
    }

    @Test 
    void testValidateTitle_empty_shouldThrow() {
        // Test that an empty title throws IllegalArgumentException with the correct message
        Exception e = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateTitle(""));
        assertEquals("Title cannot be empty.", e.getMessage());
    } 

    @Test
    void testValidateTitle_null_shouldThrow() {
        // Test that a null title throws IllegalArgumentException with the correct message
        Exception e = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateTitle(null));
        assertEquals("Title cannot be empty.", e.getMessage());
    }

    @Test
    void testValidateTitle_whitespaceOnly_shouldThrow() {
        // Test that a title with only whitespace throws IllegalArgumentException with the correct message
        Exception e = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateTitle("   "));
        assertEquals("Title cannot be empty.", e.getMessage());
    }


    @Test
    void testValidateDescription_valid() {
        // Test that a valid description does not throw an exception
        assertDoesNotThrow(() -> ValidationUtil.validateDescription("Something here"));
    }


    @Test
    void testValidateDescription_null_shouldThrow() {
        // Test that a null description throws IllegalArgumentException with the correct message
        Exception e = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateDescription(null));
        assertEquals("Description cannot be empty.", e.getMessage());
    }

    @Test
    void testValidateDescription_empty_shouldThrow() {
        // Test that an empty description throws IllegalArgumentException with the correct message
        Exception e = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateDescription(""));
        assertEquals("Description cannot be empty.", e.getMessage());
    }

    @Test
    void testValidateDescription_whitespaceOnly_shouldThrow() {
        // Test that a description with only whitespace throws IllegalArgumentException with the correct message
        Exception e = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateDescription("   "));
        assertEquals("Description cannot be empty.", e.getMessage());
    }


    @Test
    void testValidatePriority_valid() {
        // Test that a valid priority does not throw an exception
        assertDoesNotThrow(() -> ValidationUtil.validatePriority(Priority.HIGH));
        assertDoesNotThrow(() -> ValidationUtil.validatePriority(Priority.MEDIUM));
        assertDoesNotThrow(() -> ValidationUtil.validatePriority(Priority.LOW));
    }

    @Test
    void testValidatePriority_null_shouldThrow() {
        // Test that a null priority throws IllegalArgumentException with the correct message
        Exception e = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validatePriority(null));
        assertEquals("Priority is required.", e.getMessage());
    }


    @Test
    void testValidateDueDate_null_shouldThrow() {
        // Test that a null due date throws IllegalArgumentException with the correct message
        Exception e = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateDueDate(null));
        assertEquals("Due date cannot be null.", e.getMessage());
    }


    @Test
    void testValidateDueDate_past_shouldThrow() {
        // Test that a due date in the past throws IllegalArgumentException with the correct message
        LocalDate yesterday = LocalDate.now().minusDays(1);
        Exception e = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateDueDate(yesterday));
        assertEquals("Due date cannot be in the past.", e.getMessage());
    }


    @Test
    void testValidateDueDate_todayOrFuture_shouldPass() {
        // Test that today's date and a future date do not throw an exception
        assertDoesNotThrow(() -> ValidationUtil.validateDueDate(LocalDate.now()));
        assertDoesNotThrow(() -> ValidationUtil.validateDueDate(LocalDate.now().plusDays(5)));
    }
}
