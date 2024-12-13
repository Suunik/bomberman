package ee.taltech.iti0301.game.character;

public class User {
    private final String name;
    private boolean ready;

    public User(String name, boolean ready) {
        this.name = name;
        this.ready = ready;
    }

    public String getName() {
        return name;
    }

}

