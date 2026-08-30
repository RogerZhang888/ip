/** Represents one task in Alpha's task list. */
public class Task {
    private static int COUNTER = 0;
    private final int id;
    private final String description;
    private boolean done = false;

    public Task(String description) {
        this.id = COUNTER;
        Task.COUNTER ++;
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    /** Returns the text describing this task. */
    public String getDescription() {
        return this.description;
    }

    public String toString() {
        String status = this.done ? "X" : " ";
        return String.format("[%s][%s] %s%s", this.getTypeIcon(), status,
                this.description, this.getTimeDetails());
    }

    /** Returns the letter used to identify this task type in the user interface. */
    protected String getTypeIcon() {
        return "T";
    }

    /** Returns the date/time details displayed after the task description. */
    protected String getTimeDetails() {
        return "";
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
