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
            if (command.startsWith("mark ") || command.startsWith("unmark ")) {
                System.out.println(updateTaskStatus(chatbot.getList(), command, true));
                continue;
            }

            switch(command) {
                case "bye":
                    chatbot.close();
                    break;
                case "list":
                    System.out.println(chatbot.getList());
                    break;
                default:
                    System.out.println(chatbot.getList().addTask(input));
            }
        }
        scanner.close();
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
            return String.format("Nice! I've marked this task as done:%n  [X] %s", task);
        }

        list.markUndone(number);
        return String.format("OK, I've marked this task as not done yet:%n  [ ] %s", task);
    }
}
