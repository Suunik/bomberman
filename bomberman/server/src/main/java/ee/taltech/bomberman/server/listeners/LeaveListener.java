package ee.taltech.bomberman.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.bomberman.server.character.Player;
import ee.taltech.bomberman.server.handlers.EnemyHandler;
import ee.taltech.bomberman.server.handlers.LobbyHandler;
import ee.taltech.bomberman.server.handlers.PlayerHandler;
import ee.taltech.bomberman.server.handlers.World;

public class LeaveListener extends Listener {
    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
        if (LobbyHandler.INSTANCE.getUserByConnection(connection) != null) {
            LobbyHandler.INSTANCE.removeUser(LobbyHandler.INSTANCE.getUserByConnection(connection));
        }
        final Player player = PlayerHandler.INSTANCE.getPlayerByConnection(connection);
        if (player != null) {
            PlayerHandler.INSTANCE.removePlayer(player);
            if (PlayerHandler.INSTANCE.getPlayers().size() < 1) {
                LobbyHandler.INSTANCE.setStatus(false);
                JoinListener.i = 0;
                EnemyHandler.INSTANCE.removeAllEnemies();
                World.INSTANCE.removeWalls();
                LobbyHandler.INSTANCE.deleteUsers();
            }
        }
    }
}
