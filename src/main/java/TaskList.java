import java.util.ArrayList;

/** Stores and updates the tasks entered by the user. */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();

    public TaskList() {

    }

    /** Adds a plain task as a todo and returns the confirmation shown to the user. */
    public String addTask(String description) {
        return this.addTask(new Todo(description));
    }

    /** Adds any task subtype, demonstrating polymorphic storage in the collection. */
    public String addTask(Task task) {
        this.tasks.add(task);
        return String.format("Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, this.tasks.size());
    }

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
        return this.tasks.remove(number - 1);
    }

    /** Returns the number of tasks currently in the list. */
    public int size() {
        return this.tasks.size();
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
