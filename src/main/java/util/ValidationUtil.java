package util;

import entity.Priority;

import java.time.LocalDate;

public class ValidationUtil {


public static void validateTitle(String title) {
    if (title == null || title.trim().isEmpty()) {
        throw new IllegalArgumentException("Title cannot be empty.");
    }
}

public static void validateDescription(String description) {
    if (description == null || description.trim().isEmpty()) {
        throw new IllegalArgumentException("Description cannot be empty.");
    }
}

public static void validatePriority(Priority priority) {
    if (priority == null) {
        throw new IllegalArgumentException("Priority is required.");
    }
}

public static void validateDueDate(LocalDate dueDate) {
    if (dueDate == null) {
        throw new IllegalArgumentException("Due date cannot be null.");
    }
    if (dueDate.isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("Due date cannot be in the past.");
    }
}
}