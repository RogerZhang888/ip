import java.util.Scanner;

/** Runs Alpha's text-based user interface. */
public class Alpha {
    public static void main(String[] args) {
        String banner = "    _    _     ____  _   _    _    \n"
                + "   / \\  | |   |  _ \\| | | |  / \\   \n"
                + "  / _ \\ | |   | |_) | |_| | / _ \\  \n"
                + " / ___ \\| |___|  __/|  _  |/ ___ \\ \n"
                + "/_/   \\_\\_____|_|   |_| |_/_/   \\_\\\n";
        System.out.println(banner);
        Chatbot chatbot = Chatbot.getChatbot();
        System.out.println(chatbot.getGreeting());

        Scanner scanner = new Scanner(System.in);
        while (chatbot.isOpen()) {
            String input = scanner.nextLine();
            String command = input.trim();
            try {
                if (command.equals("mark") || command.startsWith("mark ")) {
                    System.out.println(updateTaskStatus(chatbot.getList(), command, true));
                    continue;
                }
                if (command.equals("unmark") || command.startsWith("unmark ")) {
                    System.out.println(updateTaskStatus(chatbot.getList(), command, false));
                    continue;
                }
                if (command.equals("delete") || command.startsWith("delete ")) {
                    System.out.println(deleteTask(chatbot.getList(), command));
                    continue;
                }

                switch (command) {
                    case "bye":
                        chatbot.close();
                        break;
                    case "list":
                        System.out.println(chatbot.getList());
                        break;
                    default:
                        System.out.println(addTypedTask(chatbot.getList(), command));
                }
            } catch (AlphaException exception) {
                System.out.println("Oops! " + exception.getMessage());
            }
        }
        scanner.close();
    }

    /** Parses a todo, deadline, or event command. */
    private static String addTypedTask(TaskList list, String input) throws AlphaException {
        if (input.isEmpty()) {
            throw new AlphaException("Please enter a command.");
        }

        String[] commandParts = input.split("\\s+", 2);
        String command = commandParts[0];
        String details = commandParts.length == 2 ? commandParts[1].trim() : "";
        switch (command) {
            case "todo":
                if (details.isEmpty()) {
                    throw new AlphaException("A todo needs a description.");
                }
                return list.addTask(new Todo(details));
            case "deadline":
                return addDeadline(list, details);
            case "event":
                return addEvent(list, details);
            default:
                throw new AlphaException("I don't recognise that command. Try todo, deadline, event, list, mark, unmark, delete, or bye.");
        }
    }

    /** Parses a deadline command using the form: deadline description /by date. */
    private static String addDeadline(TaskList list, String details) throws AlphaException {
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
        return list.addTask(new Deadline(description, by));
    }

    /** Parses an event command using the form: event description /from start /to end. */
    private static String addEvent(TaskList list, String details) throws AlphaException {
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
        return list.addTask(new Event(description, from, to));
    }

    /** Updates a task's status and returns the message shown to the user. */
    private static String updateTaskStatus(TaskList list, String command, boolean done) throws AlphaException {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            throw new AlphaException("Please provide a task number, for example: mark 2.");
        }

        int number;
        try {
            number = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new AlphaException("Task numbers must be whole numbers.");
        }

        Task task = list.getTask(number);
        if (task == null) {
            throw new AlphaException("That task number does not exist.");
        }

        if (done) {
            list.markDone(number);
            return String.format("Nice! I've marked this task as done:%n  %s", task);
        }

        list.markUndone(number);
        return String.format("OK, I've marked this task as not done yet:%n  %s", task);
    }

    /** Deletes the numbered task and returns the confirmation shown to the user. */
    private static String deleteTask(TaskList list, String command) throws AlphaException {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            throw new AlphaException("Please provide a task number, for example: delete 2.");
        }

        int number;
        try {
            number = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            throw new AlphaException("Task numbers must be whole numbers.");
        }

        Task task = list.deleteTask(number);
        if (task == null) {
            throw new AlphaException("That task number does not exist.");
        }

        return String.format("Noted. I've removed this task:%n  %s%nNow you have %d tasks in the list.",
                task, list.size());
    }
}
