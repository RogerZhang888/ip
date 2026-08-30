package alpha;

import alpha.command.Parser;
import alpha.storage.Storage;
import alpha.storage.StorageException;
import alpha.task.Task;
import alpha.task.TaskList;
import alpha.ui.Ui;
import java.nio.file.Path;

/** Coordinates Alpha's user interface, command parser, task list, and storage. */
public class Alpha {
    private static final Path DATA_FILE = Path.of("data", "duke.txt");
    private final Ui ui;
    private final Storage storage;
    private final Parser parser;
    private final TaskList tasks;

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
        this.storage = new Storage(filePath);
        this.parser = new Parser();
        this.tasks = this.loadTasks();
    }

    /** Creates Alpha using a custom task-data path and the standard UI. */
    public Alpha(Path filePath) {
        this(filePath, new Ui());
    }

    /** Runs Alpha until the user enters {@code bye} or input ends. */
    public void run() {
        this.ui.showWelcome();
        try {
            String input = this.ui.readCommand();
            while (input != null) {
                try {
                    if (!this.execute(this.parser.parse(input))) {
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

    /** Executes one parsed command. */
    private boolean execute(Parser.Command command) throws AlphaException {
        switch (command.getType()) {
            case ADD:
                Task addedTask = this.tasks.addTask(command.getTask());
                this.saveTasks();
                this.ui.showAdded(addedTask, this.tasks.size());
                break;
            case LIST:
                this.ui.showTasks(this.tasks);
                break;
            case FIND:
                this.ui.showFound(this.tasks.findTasks(command.getKeyword()));
                break;
            case MARK:
                this.updateTaskStatus(command.getTaskNumber(), true);
                break;
            case UNMARK:
                this.updateTaskStatus(command.getTaskNumber(), false);
                break;
            case DELETE:
                this.deleteTask(command.getTaskNumber());
                break;
            case BYE:
                this.ui.showGoodbye();
                return false;
            default:
                throw new AlphaException("I don't recognise that command.");
        }
        return true;
    }

    /** Marks or unmarks a task and saves the changed list. */
    private void updateTaskStatus(int number, boolean done) throws AlphaException {
        Task task = this.requireTask(number);
        if (done) {
            this.tasks.markDone(number);
        } else {
            this.tasks.markUndone(number);
        }
        this.saveTasks();
        this.ui.showMarked(task, done);
    }

    /** Deletes a task and saves the changed list. */
    private void deleteTask(int number) throws AlphaException {
        Task task = this.requireTask(number);
        this.tasks.deleteTask(number);
        this.saveTasks();
        this.ui.showDeleted(task, this.tasks.size());
    }

    /** Returns a numbered task or reports that the number is invalid. */
    private Task requireTask(int number) throws AlphaException {
        Task task = this.tasks.getTask(number);
        if (task == null) {
            throw new AlphaException("That task number does not exist.");
        }
        return task;
    }

    /** Loads saved tasks, falling back to an empty list when storage cannot be read. */
    private TaskList loadTasks() {
        try {
            return new TaskList(this.storage.load());
        } catch (StorageException exception) {
            this.ui.showLoadingError();
            return new TaskList();
        }
    }

    /** Saves the current task list and reports failures without losing in-memory changes. */
    private void saveTasks() {
        try {
            this.storage.save(this.tasks);
        } catch (StorageException exception) {
            this.ui.showSavingError();
        }
    }

    /** Starts Alpha from the command line. */
    public static void main(String[] args) {
        new Alpha().run();
    }
}
