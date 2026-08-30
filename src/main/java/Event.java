/** A task that takes place between a specified start and end time. */
public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /** Returns the start time of this event. */
    public String getFrom() {
        return this.from;
    }

    /** Returns the end time of this event. */
    public String getTo() {
        return this.to;
    }

    @Override
    protected String getTypeIcon() {
        return "E";
    }

    @Override
    protected String getTimeDetails() {
        return String.format(" (from: %s to: %s)", this.from, this.to);
    }
}
