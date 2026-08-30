/** A task that must be completed by a specified date or time. */
public class Deadline extends Task {
    private final String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /** Returns the date or time by which this task should be completed. */
    public String getBy() {
        return this.by;
    }

    @Override
    protected String getTypeIcon() {
        return "D";
    }

    @Override
    protected String getTimeDetails() {
        return String.format(" (by: %s)", this.by);
    }
}
