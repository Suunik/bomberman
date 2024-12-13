package ee.taltech.bomberman.server.handlers;

import com.esotericsoftware.kryonet.Connection;
import ee.taltech.bomberman.server.GameServer;
import ee.taltech.bomberman.server.character.Player;
import ee.taltech.iti0301.network.events.AddPlayer;
import ee.taltech.iti0301.network.events.RemovePlayer;

import java.util.LinkedList;

public class PlayerHandler {
    public static final PlayerHandler INSTANCE = new PlayerHandler();

    private final LinkedList<Player> players;

    public PlayerHandler() {
        this.players = new LinkedList<>();
    }

    public Player getPlayerByConnection(final Connection connection) {
        for (final Player player : players) {
            if (player.getConnection() == connection) {
                return player;
            }
        }
        return null;
    }

    public Player getPlayerByUsername(final String username) {
        for (final Player player : players) {
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    public void addPlayer(final Player player) {
        for (Player p : players) {
            final AddPlayer addPlayer = new AddPlayer();
            addPlayer.username = p.getUsername();
            addPlayer.x = p.getX();
            addPlayer.y = p.getY();
            player.getConnection().sendTCP(addPlayer);
        }
        final AddPlayer addPlayer = new AddPlayer();
        addPlayer.username = player.getUsername();
        addPlayer.x = player.getX();
        addPlayer.y = player.getY();
        GameServer.server.sendToAllTCP(addPlayer);
        players.add(player);
    }

    public void removePlayer(final Player player) {
        players.remove(player);
        RemovePlayer removePlayer = new RemovePlayer();
        if (PlayerHandler.INSTANCE.getPlayerByUsername(removePlayer.username) == null) return;
        removePlayer.username = player.getUsername();
        GameServer.server.sendToAllTCP(removePlayer);
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }
}
