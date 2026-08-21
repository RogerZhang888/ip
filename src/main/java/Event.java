/** A task that takes place between a specified start and end time. */
public class Event extends Task {
    private final String from;
    private final String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
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
