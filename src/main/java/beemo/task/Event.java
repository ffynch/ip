package beemo.task;

/**
 * Represents a task that occurs between a start and end date or time.
 */
public class Event extends Task {
    /** Start time of the event. */
    protected String from;
    /** End time of the event. */
    protected String to;

    /**
     * Creates an incomplete event with the specified description and times.
     *
     * @param description Description of the event.
     * @param from Start time of the event.
     * @param to End time of the event.
     */
    public Event(String description, String from, String to) {
        super(description);
        assert from != null && !from.isBlank() : "Event start time must not be blank";
        assert to != null && !to.isBlank() : "Event end time must not be blank";
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the event's start time.
     *
     * @return Event start time.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Returns the event's end time.
     *
     * @return Event end time.
     */
    public String getTo() {
        return to;
    }

    /**
     * Returns the event's type, status, description, and times for display.
     *
     * @return Display form of the event.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
