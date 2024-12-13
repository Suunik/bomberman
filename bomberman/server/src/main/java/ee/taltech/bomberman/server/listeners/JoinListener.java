package ee.taltech.bomberman.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.bomberman.server.handlers.LobbyHandler;
import ee.taltech.bomberman.server.handlers.PlayerHandler;
import ee.taltech.bomberman.server.handlers.World;
import ee.taltech.iti0301.network.events.DestructibleWalls;
import ee.taltech.iti0301.network.events.JoinRequest;
import ee.taltech.iti0301.network.events.JoinResponse;
import ee.taltech.iti0301.network.events.CurrentState;

import java.util.List;

public class JoinListener extends Listener {
    public static int i;
    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof JoinRequest) {
            final JoinRequest joinRequest = (JoinRequest) object;
            if (PlayerHandler.INSTANCE.getPlayers().size() < 4) {
                LobbyHandler.INSTANCE.addUser(joinRequest.username, connection);
                final JoinResponse joinResponse = new JoinResponse();
                joinResponse.join = true;
                List<Integer> xLocation = List.of(100, 100, 990, 990);
                List<Integer> yLocation = List.of(100, 700, 700, 100);
                if (i >= 4) {
                    i = 0;
                }
                joinResponse.x = xLocation.get(i);
                joinResponse.y = yLocation.get(i);
                i += 1;
                connection.sendTCP(joinResponse);
                CurrentState currentState = new CurrentState();
                currentState.gameStatus = LobbyHandler.INSTANCE.getStatus();
                DestructibleWalls destructibleWalls = new DestructibleWalls();
                destructibleWalls.destructibleWalls = World.INSTANCE.getDestructibleWalls();
                connection.sendUDP(currentState);
                connection.sendTCP(destructibleWalls);
            } else {
                final JoinResponse joinResponse = new JoinResponse();
                joinResponse.join = false;
                connection.sendTCP(joinResponse);
            }
        }
        super.received(connection, object);
    }
}
