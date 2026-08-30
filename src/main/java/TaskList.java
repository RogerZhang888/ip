import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/** Stores and updates the tasks entered by the user. */
public class TaskList {
    private final ArrayList<Task> tasks = new ArrayList<>();
    private final Path storagePath;

    /** Creates an in-memory task list, primarily useful when persistence is not needed. */
    public TaskList() {
        this(null);
    }

    /**
     * Creates a task list backed by the given path and loads any existing tasks from it.
     * Missing files and parent directories are handled when the list is first saved.
     */
    public TaskList(Path storagePath) {
        this.storagePath = storagePath;
        if (this.storagePath != null) {
            this.load();
        }
    }

    /** Adds a plain task as a todo and returns the confirmation shown to the user. */
    public String addTask(String description) {
        return this.addTask(new Todo(description));
    }

    /** Adds any task subtype, demonstrating polymorphic storage in the collection. */
    public String addTask(Task task) {
        this.tasks.add(task);
        this.save();
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
        Task removedTask = this.tasks.remove(number - 1);
        this.save();
        return removedTask;
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
            this.save();
        }
    }

    /** Marks the task at the given one-based list number as not done. */
    public void markUndone(int number) {
        Task task = this.getTask(number);
        if (task != null) {
            task.markUndone();
            this.save();
        }
    }

    /** Loads all valid task records from the configured file. */
    private void load() {
        if (!Files.exists(this.storagePath)) {
            return;
        }

        try {
            for (String line : Files.readAllLines(this.storagePath, StandardCharsets.UTF_8)) {
                Task task = this.parseTask(line);
                if (task != null) {
                    this.tasks.add(task);
                }
            }
        } catch (IOException exception) {
            System.err.println("Warning: Could not load tasks from " + this.storagePath + ".");
        }
    }

    /** Saves the current tasks, creating the data folder if necessary. */
    private void save() {
        if (this.storagePath == null) {
            return;
        }

        try {
            Path parent = this.storagePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(this.storagePath, this.serialiseTasks(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException exception) {
            System.err.println("Warning: Could not save tasks to " + this.storagePath + ".");
        }
    }

    /** Converts the current tasks into one file record per line. */
    private List<String> serialiseTasks() {
        ArrayList<String> lines = new ArrayList<>();
        for (Task task : this.tasks) {
            String type;
            String record = "";
            if (task instanceof Deadline deadline) {
                type = "D";
                record = "|" + encode(deadline.getBy().toString());
            } else if (task instanceof Event event) {
                type = "E";
                record = "|" + encode(event.getFrom().toString()) + "|"
                        + encode(event.getTo().toString());
            } else {
                type = "T";
            }

            lines.add(type + "|" + (task.isDone() ? "1" : "0") + "|"
                    + encode(task.getDescription()) + record);
        }
        return lines;
    }

    /** Recreates one task from a file record, or returns null for malformed records. */
    private Task parseTask(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 3 || (!parts[1].equals("0") && !parts[1].equals("1"))) {
            return null;
        }

        try {
            String description = decode(parts[2]);
            Task task;
            switch (parts[0]) {
                case "T":
                    if (parts.length != 3) {
                        return null;
                    }
                    task = new Todo(description);
                    break;
                case "D":
                    if (parts.length != 4) {
                        return null;
                    }
                    task = new Deadline(description, LocalDateTime.parse(decode(parts[3])));
                    break;
                case "E":
                    if (parts.length != 5) {
                        return null;
                    }
                    task = new Event(description, LocalDateTime.parse(decode(parts[3])),
                            LocalDateTime.parse(decode(parts[4])));
                    break;
                default:
                    return null;
            }

            if (parts[1].equals("1")) {
                task.markDone();
            }
            return task;
        } catch (IllegalArgumentException | DateTimeException exception) {
            return null;
        }
    }

    /** Encodes text so separators and whitespace in user input remain unchanged. */
    private static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    /** Decodes text previously written by {@link #encode(String)}. */
    private static String decode(String value) {
        return new String(Base64.getDecoder().decode(value), StandardCharsets.UTF_8);
    }
}
