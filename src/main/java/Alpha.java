import java.util.Objects;
import java.util.Scanner;

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
            switch(input) {
                case "bye":
                    chatbot.close();
                    break;
                case "list":
                    System.out.println(chatbot.getList());
                    break;
                default:
                    System.out.println(chatbot.getList().addAction(input));
            }
        }
        scanner.close();
    }
}
