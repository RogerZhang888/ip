package alpha;

import alpha.ui.Ui;
import java.nio.file.Path;

/** Coordinates Alpha's user interface, command parser, task list, and storage. */
public class Alpha {
    private static final Path DATA_FILE = Path.of("data", "duke.txt");
    private final Ui ui;
    private final TaskManager taskManager;

    /** Creates Alpha using the default relative task-data path. */
    public Alpha() {
        this(Alpha.DATA_FILE);
    }

    /** Creates Alpha using a custom task-data path. */
    public Alpha(String filePath) {
        this(Path.of(filePath));
    }

    /** Creates Alpha using a custom path and UI, which is useful for testing. */
    public Alpha(Path filePath, Ui ui) {
        this.ui = ui;
        this.taskManager = new TaskManager(filePath);
    }

    /** Creates Alpha using a custom task-data path and the standard UI. */
    public Alpha(Path filePath) {
        this(filePath, new Ui());
    }

    /** Runs Alpha until the user enters {@code bye} or input ends. */
    public void run() {
        this.ui.showWelcome();
        if (this.taskManager.getLoadingWarning() != null) {
            this.ui.showMessage(this.taskManager.getLoadingWarning());
        }

        try {
            String input = this.ui.readCommand();
            while (input != null) {
                try {
                    TaskManager.CommandResult result = this.taskManager.execute(input);
                    if (!result.getMessage().isEmpty()) {
                        this.ui.showMessage(result.getMessage());
                    }
                    if (result.isExit()) {
                        break;
                    }
                } catch (AlphaException exception) {
                    this.ui.showError(exception.getMessage());
                }
                input = this.ui.readCommand();
            }
        } finally {
            this.ui.close();
        }
    }

    /** Starts Alpha from the command line. */
    public static void main(String[] args) {
        new Alpha().run();
    }
}
