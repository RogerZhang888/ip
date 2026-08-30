package alpha.ui;

import alpha.task.Task;
import alpha.task.TaskList;
import java.util.Scanner;

/** Handles all interaction between Alpha and the user. */
public class Ui {
    private static final String BANNER = "    _    _     ____  _   _    _    \n"
            + "   / \\  | |   |  _ \\| | | |  / \\   \n"
            + "  / _ \\ | |   | |_) | |_| | / _ \\  \n"
            + " / ___ \\| |___|  __/|  _  |/ ___ \\ \n"
            + "/_/   \\_\\_____|_|   |_| |_/_/   \\_\\\n";
    private static final String GREETING = "Hello! I am Alpha. What can I do for you?";
    private static final String EXIT_MESSAGE = "Bye. Hope to see you again!";
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
        System.out.println("Oops! " + message);
    }

    /** Shows the current tasks. */
    public void showTasks(TaskList tasks) {
        System.out.println(tasks);
    }

    /** Shows confirmation after adding a task. */
    public void showAdded(Task task, int taskCount) {
        this.showMessage(String.format("Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, taskCount));
    }

    /** Shows confirmation after marking a task. */
    public void showMarked(Task task, boolean done) {
        String message = done
                ? "Nice! I've marked this task as done:"
                : "OK, I've marked this task as not done yet:";
        this.showMessage(String.format("%s%n  %s", message, task));
    }

    /** Shows confirmation after deleting a task. */
    public void showDeleted(Task task, int taskCount) {
        this.showMessage(String.format("Noted. I've removed this task:%n  %s%nNow you have %d tasks in the list.",
                task, taskCount));
    }

    /** Shows a problem encountered while loading saved tasks. */
    public void showLoadingError() {
        this.showMessage("Warning: Could not load saved tasks. Starting with an empty task list.");
    }

    /** Shows a problem encountered while saving tasks. */
    public void showSavingError() {
        this.showMessage("Warning: Could not save tasks.");
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
