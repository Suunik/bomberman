package ee.taltech.iti0301.network.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.iti0301.game.BombermanGdxGame;

public class ConnectionStateListener extends Listener {

    @Override
    public void disconnected(Connection connection) {
        System.out.println("Connection lost!");
        BombermanGdxGame.client.close();
        super.disconnected(connection);
    }
}
