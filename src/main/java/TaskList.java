public class TaskList {
    private Action[] actions = new Action[100];
    private int next = 0;

    public TaskList() {

    }

    public String addAction(String description) {
        Action action = new Action(description);
        this.actions[this.next] = action;
        this.next++;
        return String.format("added: %s", action.toString());
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.next; i++) {
            builder.append(String.format("%d: %s \n", i+1, actions[i].toString()));
        }

        return builder.toString();
    }
}
