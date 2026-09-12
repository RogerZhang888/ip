package alpha.ui;

import alpha.task.Task;
import alpha.task.TaskList;
import java.util.List;
import java.util.Scanner;

/** Handles all interaction between Alpha and the user. */
public class Ui {
    private static final String BANNER = " __  __   ___   ____ ____\n"
            + "|  \\/  | / _ \\ / ___/ ___|\n"
            + "| |\\/| || | | |\\___ \\___ \\n"
            + "| |  | || |_| | ___) |___) |\n"
            + "|_|  |_| \\___/ |____/|____/\n";
    private static final String GREETING = "Hello! I'm Moss, your quiet task gardener. What shall we tend today?";
    private static final String EXIT_MESSAGE = "The garden can rest now. See you next time!";
    private final Scanner scanner;

    /** Creates a UI that reads commands from standard input. */
    public Ui() {
        this(new Scanner(System.in));
    }

    /** Creates a UI with the supplied input source, which is useful for testing. */
    public Ui(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Shows Alpha's banner and greeting. */
    public void showWelcome() {
        System.out.println(BANNER);
        System.out.println(GREETING);
    }

    /** Reads the next command, or returns null when input has ended. */
    public String readCommand() {
        return this.scanner.hasNextLine() ? this.scanner.nextLine() : null;
    }

    /** Shows a normal response to the user. */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /** Shows a command error to the user. */
    public void showError(String message) {
        System.out.println("Moss says: " + message);
    }

    /** Shows the current tasks. */
    public void showTasks(TaskList tasks) {
        System.out.println(tasks);
    }

    /** Shows the tasks whose descriptions match a search keyword. */
    public void showFound(List<Task> matchingTasks) {
        if (matchingTasks.isEmpty()) {
            this.showMessage("Nothing sprouted for that search.");
            return;
        }

        StringBuilder builder = new StringBuilder("Here's what Moss found in your garden:");
        for (int i = 0; i < matchingTasks.size(); i++) {
            builder.append(String.format("%n%d.%s", i + 1, matchingTasks.get(i)));
        }
        this.showMessage(builder.toString());
    }

    /** Shows confirmation after adding a task. */
    public void showAdded(Task task, int taskCount) {
        this.showMessage(String.format("Planted your task:%n  %s%nYour garden now has %d tasks.",
                task, taskCount));
    }

    /** Shows confirmation after marking a task. */
    public void showMarked(Task task, boolean done) {
        String message = done
                ? "Nicely tended—this task is complete:"
                : "No worries—I've reopened this task:";
        this.showMessage(String.format("%s%n  %s", message, task));
    }

    /** Shows confirmation after deleting a task. */
    public void showDeleted(Task task, int taskCount) {
        this.showMessage(String.format("Pruned this task:%n  %s%nYour garden now has %d tasks.",
                task, taskCount));
    }

    /** Shows a problem encountered while loading saved tasks. */
    public void showLoadingError() {
        this.showMessage("Moss couldn't reopen your task garden, so we're starting with a fresh patch.");
    }

    /** Shows a problem encountered while saving tasks. */
    public void showSavingError() {
        this.showMessage("Moss couldn't save the latest garden changes.");
    }

    /** Shows the exit message. */
    public void showGoodbye() {
        this.showMessage(EXIT_MESSAGE);
    }

    /** Releases the input resource. */
    public void close() {
        this.scanner.close();
    }
}
