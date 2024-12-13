package ee.taltech.bomberman.server;

import com.esotericsoftware.kryonet.Server;
import ee.taltech.bomberman.server.handlers.EnemyUpdateHandler;
import ee.taltech.bomberman.server.handlers.PlayerUpdateHandler;
import ee.taltech.bomberman.server.listeners.EventListener;
import ee.taltech.bomberman.server.listeners.JoinListener;
import ee.taltech.bomberman.server.listeners.LeaveListener;
import ee.taltech.iti0301.network.Network;

import java.io.IOException;

public class GameServer {
    public static Server server;

    public GameServer() {
        server = new Server();
        Network.register(server);
        server.addListener(new JoinListener());
        server.addListener(new LeaveListener());
        server.addListener(new EventListener());
        PlayerUpdateHandler.INSTANCE.start();
        EnemyUpdateHandler.INSTANCE.start();
        server.start();
        try {
            server.bind(Network.TCP, Network.UDP);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new GameServer();
    }
}
