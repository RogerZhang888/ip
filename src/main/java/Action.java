public class Action {
    private static int COUNTER = 0;
    private int id;
    private String description;

    public Action(String description) {
        this.id = COUNTER;
        Action.COUNTER ++;
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public String toString() {
        return this.description;
    }
}
