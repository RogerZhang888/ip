/** Represents one task in Alpha's task list. */
public class Task {
    private static int COUNTER = 0;
    private int id;
    private String description;
    private boolean done = false;

    public Task(String description) {
        this.id = COUNTER;
        Task.COUNTER ++;
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return this.description;
    }

    public void markDone() {
        this.done = true;
    }

    /** Changes this task back to an unfinished state. */
    public void markUndone() {
        this.done = false;
    }

    public boolean isDone() {
        return this.done;
    }
}
