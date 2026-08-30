package alpha.command;

import alpha.AlphaException;
import alpha.task.Deadline;
import alpha.task.DateTimeParser;
import alpha.task.Event;
import alpha.task.Task;
import alpha.task.Todo;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/** Parses user input into commands that Alpha can execute. */
public class Parser {
    /** The operations understood by Alpha. */
    public enum CommandType {
        ADD,
        BYE,
        DELETE,
        FIND,
        LIST,
        MARK,
        UNMARK
    }

    /** Immutable result of parsing one user command. */
    public static class Command {
        private final CommandType type;
        private final Task task;
        private final int taskNumber;
        private final String keyword;

        private Command(CommandType type, Task task, int taskNumber) {
            this(type, task, taskNumber, null);
        }

        private Command(CommandType type, String keyword) {
            this(type, null, 0, keyword);
        }

        private Command(CommandType type, Task task, int taskNumber, String keyword) {
            this.type = type;
            this.task = task;
            this.taskNumber = taskNumber;
            this.keyword = keyword;
        }

        /** Returns the command operation. */
        public CommandType getType() {
            return this.type;
        }

        /** Returns the task for an add command. */
        public Task getTask() {
            return this.task;
        }

        /** Returns the task number for a mark, unmark, or delete command. */
        public int getTaskNumber() {
            return this.taskNumber;
        }

        /** Returns the search keyword for a find command. */
        public String getKeyword() {
            return this.keyword;
        }
    }

    /** Parses one complete user input line. */
    public Command parse(String input) throws AlphaException {
        String command = input.trim();
        if (command.isEmpty()) {
            throw new AlphaException("Please enter a command.");
        }

        if (command.equals("bye")) {
            return new Command(CommandType.BYE, null, 0);
        }
        if (command.equals("list")) {
            return new Command(CommandType.LIST, null, 0);
        }
        if (command.equals("find") || command.startsWith("find ")) {
            return new Command(CommandType.FIND, parseKeyword(command));
        }
        if (command.equals("mark") || command.startsWith("mark ")) {
            return new Command(CommandType.MARK, null, parseTaskNumber(command));
        }
        if (command.equals("unmark") || command.startsWith("unmark ")) {
            return new Command(CommandType.UNMARK, null, parseTaskNumber(command));
        }
        if (command.equals("delete") || command.startsWith("delete ")) {
            return new Command(CommandType.DELETE, null, parseTaskNumber(command));
        }

        String[] commandParts = command.split("\\s+", 2);
        String commandWord = commandParts[0];
        String details = commandParts.length == 2 ? commandParts[1].trim() : "";
        switch (commandWord) {
            case "todo":
                return new Command(CommandType.ADD, parseTodo(details), 0);
            case "deadline":
                return new Command(CommandType.ADD, parseDeadline(details), 0);
            case "event":
                return new Command(CommandType.ADD, parseEvent(details), 0);
            default:
                throw new AlphaException(
                        "I don't recognise that command. Try todo, deadline, event, list, "
                                + "find, mark, unmark, delete, or bye.");
        }
    }

    /** Parses the description of a todo. */
    private Task parseTodo(String details) throws AlphaException {
        if (details.isEmpty()) {
            throw new AlphaException("A todo needs a description.");
        }
        return new Todo(details);
    }

    /** Parses a deadline using the form: deadline description /by date or time. */
    private Task parseDeadline(String details) throws AlphaException {
        int markerIndex = details.indexOf("/by");
        if (markerIndex < 0) {
            throw new AlphaException("A deadline needs a description followed by /by and a date or time.");
        }

        String description = details.substring(0, markerIndex).trim();
        String by = details.substring(markerIndex + 3).trim();
        if (description.isEmpty()) {
            throw new AlphaException("A deadline needs a description.");
        }
        if (by.isEmpty()) {
            throw new AlphaException("A deadline needs a date or time after /by.");
        }
        return new Deadline(description, parseDateTime(by));
    }

    /** Parses an event using the form: event description /from start /to end. */
    private Task parseEvent(String details) throws AlphaException {
        int fromIndex = details.indexOf("/from");
        int toIndex = details.indexOf("/to", fromIndex + 5);
        if (fromIndex < 0) {
            throw new AlphaException("An event needs a description followed by /from and /to times.");
        }
        if (toIndex < 0) {
            throw new AlphaException("An event needs an end time after /to.");
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + 5, toIndex).trim();
        String to = details.substring(toIndex + 3).trim();
        if (description.isEmpty()) {
            throw new AlphaException("An event needs a description.");
        }
        if (from.isEmpty()) {
            throw new AlphaException("An event needs a start time after /from.");
        }
        if (to.isEmpty()) {
            throw new AlphaException("An event needs an end time after /to.");
        }
        return new Event(description, parseDateTime(from), parseDateTime(to));
    }

    /** Parses the supported date/time formats and converts failures into user errors. */
    private LocalDateTime parseDateTime(String value) throws AlphaException {
        try {
            return DateTimeParser.parse(value);
        } catch (DateTimeParseException exception) {
            throw new AlphaException("Please use a date such as 2019-10-15 or a date/time such as 2/12/2019 1800.");
        }
    }

    /** Parses a one-based task number from a mark, unmark, or delete command. */
    private int parseTaskNumber(String command) throws AlphaException {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            throw new AlphaException("Please provide a task number, for example: " + parts[0] + " 2.");
        }

        try {
            return Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new AlphaException("Task numbers must be whole numbers.");
        }
    }

    /** Parses the non-empty keyword from a find command. */
    private String parseKeyword(String command) throws AlphaException {
        String[] parts = command.split("\\s+", 2);
        if (parts.length != 2 || parts[1].trim().isEmpty()) {
            throw new AlphaException("Please provide a keyword to search for, for example: find book.");
        }
        return parts[1].trim();
    }
}
