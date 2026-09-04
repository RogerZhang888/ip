package alpha;

import alpha.command.Parser;
import alpha.storage.Storage;
import alpha.storage.StorageException;
import alpha.task.Task;
import alpha.task.TaskList;
import java.nio.file.Path;
import java.util.List;

/** Coordinates command execution, task updates, and task persistence. */
public class TaskManager {
    private static final Path DATA_FILE = Path.of("data", "duke.txt");
    private final Storage storage;
    private final Parser parser;
    private final TaskList tasks;
    private final String loadingWarning;

    /** Creates a task manager using the default relative task-data path. */
    public TaskManager() {
        this(TaskManager.DATA_FILE);
    }

    /** Creates a task manager using the supplied task-data path. */
    public TaskManager(Path filePath) {
        this.storage = new Storage(filePath);
        this.parser = new Parser();

        TaskList loadedTasks;
        String warning;
        try {
            loadedTasks = new TaskList(this.storage.load());
            warning = null;
        } catch (StorageException exception) {
            loadedTasks = new TaskList();
            warning = "Warning: Could not load saved tasks. Starting with an empty task list.";
        }
        this.tasks = loadedTasks;
        this.loadingWarning = warning;
    }

    /** Represents the text and exit state produced by one valid command. */
    public static class CommandResult {
        private final String message;
        private final boolean exit;

        /** Creates a command result. */
        public CommandResult(String message, boolean exit) {
            this.message = message;
            this.exit = exit;
        }

        /** Returns the response text for the command. */
        public String getMessage() {
            return this.message;
        }

        /** Returns whether the application should close after this command. */
        public boolean isExit() {
            return this.exit;
        }
    }

    /** Returns a warning produced while loading saved tasks, or {@code null}. */
    public String getLoadingWarning() {
        return this.loadingWarning;
    }

    /** Returns a read-only view of the current tasks. */
    public List<Task> getTasks() {
        return this.tasks.getTasks();
    }

    /** Executes one command and returns the response that should be shown to the user. */
    public CommandResult execute(String input) throws AlphaException {
        Parser.Command command = this.parser.parse(input);
        switch (command.getType()) {
            case ADD:
                return this.addTask(command.getTask());
            case LIST:
                return new CommandResult(this.tasks.toString(), false);
            case FIND:
                return this.findTasks(command.getKeyword());
            case MARK:
                return this.updateTaskStatus(command.getTaskNumber(), true);
            case UNMARK:
                return this.updateTaskStatus(command.getTaskNumber(), false);
            case DELETE:
                return this.deleteTask(command.getTaskNumber());
            case BYE:
                return new CommandResult("Bye. Hope to see you again!", true);
            default:
                throw new AlphaException("I don't recognise that command.");
        }
    }

    /** Adds a task, saves it, and creates the corresponding response. */
    private CommandResult addTask(Task task) {
        Task addedTask = this.tasks.addTask(task);
        String message = String.format("Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                addedTask, this.tasks.size());
        return new CommandResult(this.withSavingWarning(message), false);
    }

    /** Finds tasks by keyword and creates the corresponding response. */
    private CommandResult findTasks(String keyword) {
        List<Task> matchingTasks = this.tasks.findTasks(keyword);
        if (matchingTasks.isEmpty()) {
            return new CommandResult("No matching tasks found.", false);
        }

        StringBuilder builder = new StringBuilder("Here are the matching tasks in your list:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            builder.append(String.format("%n%d.%s", i + 1, matchingTasks.get(i)));
        }
        return new CommandResult(builder.toString(), false);
    }

    /** Marks or unmarks a task, saves the changed list, and creates a response. */
    private CommandResult updateTaskStatus(int number, boolean done) throws AlphaException {
        Task task = this.requireTask(number);
        if (done) {
            this.tasks.markDone(number);
        } else {
            this.tasks.markUndone(number);
        }

        String message = done
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
        message = String.format("%s%n  %s", message, task);
        return new CommandResult(this.withSavingWarning(message), false);
    }

    /** Deletes a task, saves the changed list, and creates a response. */
    private CommandResult deleteTask(int number) throws AlphaException {
        Task task = this.requireTask(number);
        this.tasks.deleteTask(number);
        String message = String.format("Noted. I've removed this task:%n  %s%nNow you have %d tasks in the list.",
                task, this.tasks.size());
        return new CommandResult(this.withSavingWarning(message), false);
    }

    /** Returns a numbered task or reports that the number is invalid. */
    private Task requireTask(int number) throws AlphaException {
        Task task = this.tasks.getTask(number);
        if (task == null) {
            throw new AlphaException("That task number does not exist.");
        }
        return task;
    }

    /** Saves tasks and appends a warning if persistence fails. */
    private String withSavingWarning(String message) {
        try {
            this.storage.save(this.tasks);
            return message;
        } catch (StorageException exception) {
            return message + System.lineSeparator() + "Warning: Could not save tasks.";
        }
    }
}
