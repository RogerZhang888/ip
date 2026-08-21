/** Stores and updates the tasks entered by the user. */
public class TaskList {
    private Task[] tasks = new Task[100];
    private int next = 0;

    public TaskList() {

    }

    public String addTask(String description) {
        Task task = new Task(description);
        this.tasks[this.next] = task;
        this.next++;
        return String.format("added: %s", task.toString());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.next; i++) {
            String done = tasks[i].isDone() ? "X" : " ";
            builder.append(String.format("%d: [%s] %s \n", i+1, done, tasks[i].toString()));
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
