package ee.taltech.iti0301.network.handlers;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.game.BombermanGdxGame;
import ee.taltech.iti0301.game.character.Player;
import ee.taltech.iti0301.game.gameState.GameState;
import ee.taltech.iti0301.network.events.AddPlayer;
import ee.taltech.iti0301.network.events.PlayerUpdate;
import ee.taltech.iti0301.network.events.RemoveCollidedPlayers;



import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class PlayerHandler {
    public static final PlayerHandler INSTANCE = new PlayerHandler();
    private LinkedList<Player> players = new LinkedList<>();
    private Vector2 position;
    private String username;

    public Player getPlayerByUsername(final String username) {
        for (int i = 0; i < players.size(); i++) {
            final Player player = players.get(i);
            if (player.getUsername().equals(username)) {
                return player;
            }
        }
        return null;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void newPlayer(final Player player) {
        AddPlayer addPlayer = new AddPlayer();
        addPlayer.username = player.getUsername();
        addPlayer.x = position.x;
        addPlayer.y = position.y;
        players.add(player);
        BombermanGdxGame.client.sendTCP(addPlayer);
    }

    public void removePlayer(final Player player) {
        players.remove(player);
        GameState.INSTANCE.removeCharacter(player);
    }

    public void movePlayer(final Player player) {
        final PlayerUpdate playerUpdate = new PlayerUpdate();
        playerUpdate.username = player.getUsername();
        playerUpdate.x = player.getPosition().x;
        playerUpdate.y = player.getPosition().y;
        BombermanGdxGame.client.sendTCP(playerUpdate);
    }

    public void removeCollidedPlayers(final Set<Player> characterSet) {
        RemoveCollidedPlayers removeCollidedPlayers = new RemoveCollidedPlayers();
        Set<String> names = new HashSet<>();
        players.removeAll(characterSet);
        for (Player player : characterSet) {
            names.add(player.getUsername());
        }
        removeCollidedPlayers.names = names;
        GameState.INSTANCE.removeAllCharacters(characterSet);
        BombermanGdxGame.client.sendUDP(removeCollidedPlayers);
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }


    public void emptyPlayersList() {
        players.clear();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setUsername(String name) {
        username = name;
    }

    public String getUsername() {
        return username;
    }
}
