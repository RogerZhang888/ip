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
            if (command.startsWith("mark ")) {
                System.out.println(updateTaskStatus(chatbot.getList(), command, true));
                continue;
            }
            if (command.startsWith("unmark ")) {
                System.out.println(updateTaskStatus(chatbot.getList(), command, false));
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
                    System.out.println(addTypedTask(chatbot.getList(), input));
            }
        }
        scanner.close();
    }

    /** Parses a todo, deadline, event, or legacy plain task command. */
    private static String addTypedTask(TaskList list, String input) {
        String[] commandParts = input.trim().split("\\s+", 2);
        if (commandParts.length == 1) {
            return list.addTask(input);
        }

        String command = commandParts[0];
        String details = commandParts[1].trim();
        switch (command) {
            case "todo":
                return list.addTask(new Todo(details));
            case "deadline":
                return addDeadline(list, details);
            case "event":
                return addEvent(list, details);
            default:
                return list.addTask(input);
        }
    }

    /** Parses a deadline command using the form: deadline description /by date. */
    private static String addDeadline(TaskList list, String details) {
        String marker = " /by ";
        int markerIndex = details.indexOf(marker);
        if (markerIndex <= 0 || markerIndex + marker.length() >= details.length()) {
            return "Please use: deadline <description> /by <date or time>.";
        }

        String description = details.substring(0, markerIndex).trim();
        String by = details.substring(markerIndex + marker.length()).trim();
        return list.addTask(new Deadline(description, by));
    }

    /** Parses an event command using the form: event description /from start /to end. */
    private static String addEvent(TaskList list, String details) {
        String fromMarker = " /from ";
        String toMarker = " /to ";
        int fromIndex = details.indexOf(fromMarker);
        int toIndex = details.indexOf(toMarker, fromIndex + fromMarker.length());
        if (fromIndex <= 0 || toIndex <= fromIndex + fromMarker.length()
                || toIndex + toMarker.length() >= details.length()) {
            return "Please use: event <description> /from <start> /to <end>.";
        }

        String description = details.substring(0, fromIndex).trim();
        String from = details.substring(fromIndex + fromMarker.length(), toIndex).trim();
        String to = details.substring(toIndex + toMarker.length()).trim();
        return list.addTask(new Event(description, from, to));
    }

    /** Updates a task's status and returns the message shown to the user. */
    private static String updateTaskStatus(TaskList list, String command, boolean done) {
        String[] parts = command.split("\\s+");
        if (parts.length != 2) {
            return "Please specify a valid task number.";
        }

        int number;
        try {
            number = Integer.parseInt(parts[1]);
        } catch (NumberFormatException exception) {
            return "Please specify a valid task number.";
        }

        Task task = list.getTask(number);
        if (task == null) {
            return "Please specify a valid task number.";
        }

        if (done) {
            list.markDone(number);
            return String.format("Nice! I've marked this task as done:%n  %s", task);
        }

        list.markUndone(number);
        return String.format("OK, I've marked this task as not done yet:%n  %s", task);
    }
}
