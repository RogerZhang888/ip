package alpha.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Stores and updates the tasks entered by the user. */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /** Creates an empty task list. */
    public TaskList() {
    }

    /** Creates a task list containing a copy of the supplied tasks. */
    public TaskList(List<Task> tasks) {
        this.tasks.addAll(tasks);
    }

    /** Adds a plain task as a todo and returns the added task. */
    public Task addTask(String description) {
        return this.addTask(new Todo(description));
    }

    /** Adds any task subtype and returns the added task. */
    public Task addTask(Task task) {
        this.tasks.add(task);
        return task;
    }

    /** Returns a numbered display of all tasks in this list. */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.tasks.size(); i++) {
            builder.append(String.format("%d: %s%n", i + 1, this.tasks.get(i)));
        }

        return builder.toString();
    }

    /** Returns the task at a one-based list number, or {@code null} if it is invalid. */
    public Task getTask(int number) {
        if (number < 1 || number > this.tasks.size()) {
            return null;
        }
        return this.tasks.get(number - 1);
    }

    /** Removes and returns the task at a one-based list number, or {@code null} if invalid. */
    public Task deleteTask(int number) {
        if (number < 1 || number > this.tasks.size()) {
            return null;
        }
        Task removedTask = this.tasks.remove(number - 1);
        return removedTask;
    }

    /** Returns the number of tasks currently in the list. */
    public int size() {
        return this.tasks.size();
    }

    /** Returns a read-only view of the tasks for persistence and display. */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(this.tasks);
    }

    /** Marks the task at the given one-based list number as done. */
    public void markDone(int number) {
        Task task = this.getTask(number);
        if (task != null) {
            task.markDone();
        }
    }

    /** Marks the task at the given one-based list number as not done. */
    public void markUndone(int number) {
        Task task = this.getTask(number);
        if (task != null) {
            task.markUndone();
        }
    }
}
