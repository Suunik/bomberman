package ee.taltech.bomberman.server.character;

import com.esotericsoftware.kryonet.Connection;

public class User {
    private final String name;
    private boolean ready;
    private Connection connection;

    public User(String name, boolean ready, Connection connection) {
        this.name = name;
        this.ready = ready;
        this.connection = connection;
    }

    public String getName() {
        return name;
    }

    public boolean getStatus() {
        return ready;
    }
    public Connection getConnection() {
        return connection;
    }
}
