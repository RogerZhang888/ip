package alpha.ui;

import java.util.Scanner;

/** Handles console input and output between Moss and the user. */
public class Ui {
    private static final String BANNER = " __  __   ___   ____ ____\n"
            + "|  \\/  | / _ \\ / ___/ ___|\n"
            + "| |\\/| || | | |\\___ \\___ \\n"
            + "| |  | || |_| | ___) |___) |\n"
            + "|_|  |_| \\___/ |____/|____/\n";
    private static final String GREETING = "Hello! I'm Moss, your quiet task gardener. What shall we tend today?";
    private final Scanner scanner;

    /** Creates a UI that reads commands from standard input. */
    public Ui() {
        this(new Scanner(System.in));
    }

    /** Creates a UI with the supplied input source, which is useful for testing. */
    public Ui(Scanner scanner) {
        this.scanner = scanner;
    }

    /** Shows Moss's banner and greeting. */
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

    /** Releases the input resource. */
    public void close() {
        this.scanner.close();
    }
}
