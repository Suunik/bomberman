package ee.taltech.iti0301.network.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import ee.taltech.iti0301.network.Network;
import ee.taltech.iti0301.network.listeners.ConnectionStateListener;
import ee.taltech.iti0301.network.listeners.EventListener;

import javax.swing.JOptionPane;
import java.io.IOException;

public class GameClient {

    private static Client client;

    public GameClient() {
        client = new Client();
        Network.register(client);
        client.addListener(new ConnectionStateListener());
        client.addListener(new EventListener());
        client.start();
        String ip = "localhost";
        // String ip = "193.40.156.86";
        try {
            client.connect(5000, ip, Network.TCP, Network.UDP);
        } catch (IOException e) {
            e.printStackTrace();
            int result = JOptionPane.showConfirmDialog(null, "Unable to connect to " + ip,
                    "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (result == 0) System.exit(0);
        }
    }

    public void close() {
        client.close();
        Gdx.app.exit();
    }

    public void sendTCP(Object object) {
        client.sendTCP(object);
    }

    public void sendUDP(Object object) {
        client.sendUDP(object);
    }
}
