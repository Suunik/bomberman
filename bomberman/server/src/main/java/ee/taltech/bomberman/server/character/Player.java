package ee.taltech.bomberman.server.character;

import com.esotericsoftware.kryonet.Connection;

public class Player {

    private final String username;
    private final Connection connection;
    private float x;
    private float y;

    public Player(String username, Connection connection) {
        this.username = username;
        this.connection = connection;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getUsername() {
        return username;
    }

    public Connection getConnection() {
        return connection;
    }
}
