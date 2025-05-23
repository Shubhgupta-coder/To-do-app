package entity;

import java.time.LocalDate;
import java.util.UUID;


public class Todo {
private final String id;
private String title;
private String description;
private Priority priority;
private LocalDate dueDate;
private boolean completed;
private final LocalDate createdDate;

public Todo(String title, String description, Priority priority, LocalDate dueDate) {
    this.id = UUID.randomUUID().toString();
    this.title = title;
    this.description = description;
    this.priority = priority;
    this.dueDate = dueDate;
    this.completed = false;
    this.createdDate = LocalDate.now();
}

public Todo(String id, String title, String description, Priority priority, LocalDate dueDate, LocalDate createdDate, boolean completed) {
this.id = id;
this.title = title;
this.description = description;
this.priority = priority;
this.dueDate = dueDate;
this.createdDate = createdDate;
this.completed = completed;
}

// Getters and Setters
public String getId() {
    return id;
}

public String getTitle() {
    return title;
}

public void setTitle(String title) {
    this.title = title;
}

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public Priority getPriority() {
    return priority;
}

public void setPriority(Priority priority) {
    this.priority = priority;
}

public LocalDate getDueDate() {
    return dueDate;
}

public void setDueDate(LocalDate dueDate) {
    this.dueDate = dueDate;
}

public boolean isCompleted() {
    return completed;
}

public void setCompleted(boolean completed) {
    this.completed = completed;
}

public LocalDate getCreatedDate() {
    return createdDate;
}

@Override
public String toString() {
    return String.format("Todo[id=%s, title=%s, priority=%s, due=%s, complete=%s]",
            id, title, priority, dueDate, completed);
}
}
// public enum Priority {
//LOW,
//MEDIUM,
//HIGH
//}