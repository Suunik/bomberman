package ee.taltech.bomberman.server.character;

import com.esotericsoftware.kryonet.Connection;

public class Enemy {
    private boolean w;
    private boolean a;
    private boolean s;
    private boolean d;
    private float x;
    private float y;

    private final Connection connection;

    public Enemy(Connection connection) {
        this.connection = connection;
    }

    public void setA(boolean a) {
        this.a = a;
    }

    public void setD(boolean d) {
        this.d = d;
    }

    public void setS(boolean s) {
        this.s = s;
    }

    public void setW(boolean w) {
        this.w = w;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isW() {
        return w;
    }

    public boolean isA() {
        return a;
    }

    public boolean isS() {
        return s;
    }

    public boolean isD() {
        return d;
    }

    public Connection getConnection() {
        return connection;
    }

    public int getConnectionID() {
        return connection.getID();
    }
    
}
