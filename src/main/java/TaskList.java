/** Stores and updates the tasks entered by the user. */
public class TaskList {
    private Task[] tasks = new Task[100];
    private int next = 0;

    public TaskList() {

    }

    /** Adds a plain task as a todo and returns the confirmation shown to the user. */
    public String addTask(String description) {
        return this.addTask(new Todo(description));
    }

    /** Adds any task subtype, demonstrating polymorphic storage in the task array. */
    public String addTask(Task task) {
        this.tasks[this.next] = task;
        this.next++;
        return String.format("Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, this.next);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.next; i++) {
            builder.append(String.format("%d: %s%n", i + 1, this.tasks[i]));
        }

        return builder.toString();
    }

    /** Returns the task at a one-based list number, or {@code null} if it is invalid. */
    public Task getTask(int number) {
        if (number < 1 || number > this.next) {
            return null;
        }
        return this.tasks[number - 1];
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
