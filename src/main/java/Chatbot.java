import java.nio.file.Path;

public class Chatbot {
    // singleton (there can only be one instance of Chatbot)
    private static Chatbot instance;
    private static String name = "Alpha";
    private static String greeting = String.format("Hello! I am %s. What can I do for you?", Chatbot.name);
    private static String exit = String.format("Bye. Hope to see you again!");
    private static boolean open = false;
    private static final Path DATA_FILE = Path.of("data", "duke.txt");
    private static TaskList list;

    private Chatbot() {
        Chatbot.list = new TaskList(Chatbot.DATA_FILE);
    }

    public static synchronized Chatbot getChatbot() {
        if (Chatbot.instance == null) {
            Chatbot.instance = new Chatbot();
        }
        Chatbot.open = true;
        return Chatbot.instance;
    }

    public String getGreeting() {
        return Chatbot.greeting;
    }

    public static String getExit() {
        return Chatbot.exit;
    }

    public boolean isOpen() {
        return Chatbot.open;
    }

    public TaskList getList() {
        return Chatbot.list;
    }

    public void close() {
        Chatbot.open = false;
        System.out.println(Chatbot.getExit());
    }

}
