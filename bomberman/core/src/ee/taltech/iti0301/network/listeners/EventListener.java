package ee.taltech.iti0301.network.listeners;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.iti0301.game.bomb.Bomb;
import ee.taltech.iti0301.game.character.Ai;
import ee.taltech.iti0301.game.character.Player;
import ee.taltech.iti0301.game.gameState.GameState;
import ee.taltech.iti0301.game.map.World;
import ee.taltech.iti0301.network.events.*;
import ee.taltech.iti0301.network.handlers.BombHandler;
import ee.taltech.iti0301.network.handlers.EnemyHandler;
import ee.taltech.iti0301.network.handlers.LobbyHandler;
import ee.taltech.iti0301.network.handlers.PlayerHandler;

import javax.swing.*;

public class EventListener extends Listener {
    @Override
    public void received(Connection connection, Object object) {

        if (object instanceof AddPlayer) {
            AddPlayer addPlayer = (AddPlayer) object;
            final Player player = new Player(addPlayer.username);
            if (PlayerHandler.INSTANCE.getPlayerByUsername(player.getUsername()) != null) return;
            player.setCharacterPosition(addPlayer.x, addPlayer.y);
            PlayerHandler.INSTANCE.addPlayer(player);

        } else if (object instanceof RemovePlayer) {
            RemovePlayer removePlayer = (RemovePlayer) object;
            PlayerHandler.INSTANCE.removePlayer(PlayerHandler.INSTANCE.getPlayerByUsername(removePlayer.username));

        } else if (object instanceof PlayerUpdate) {
            PlayerUpdate playerUpdate = (PlayerUpdate) object;
            Player player = PlayerHandler.INSTANCE.getPlayerByUsername(playerUpdate.username);
            if (player == null) return;
            player.setCharacterPosition(playerUpdate.x, playerUpdate.y);

        } else if (object instanceof AddBomb) {
            AddBomb addBomb = (AddBomb) object;
            Bomb bomb = new Bomb(new Vector2(addBomb.x, addBomb.y), addBomb.size);
            BombHandler.INSTANCE.addBomb(bomb);

        } else if (object instanceof JoinResponse) {
            JoinResponse joinResponse = (JoinResponse) object;
            if (!joinResponse.join) {
                JOptionPane.showMessageDialog(null, "Maximum number of players are already in the game!",
                        "Message", JOptionPane.PLAIN_MESSAGE);
            } else {
                PlayerHandler.INSTANCE.setPosition(new Vector2(joinResponse.x, joinResponse.y));
            }

        } else if (object instanceof AddEnemy) {
            AddEnemy addEnemy = (AddEnemy) object;
            EnemyHandler.INSTANCE.setEnemyPosition(addEnemy.x, addEnemy.y);

        } else if (object instanceof EnemyMoveUpdate) {
            EnemyMoveUpdate enemyMoveUpdate = (EnemyMoveUpdate) object;
            try {
                Ai enemy = EnemyHandler.INSTANCE.getEnemies().get(0);
                enemy.setServerDirection(enemyMoveUpdate.up, enemyMoveUpdate.left, enemyMoveUpdate.down, enemyMoveUpdate.right);
            } catch (IndexOutOfBoundsException e) {
                GameState.INSTANCE.emptyEnemyList();
            }

        } else if (object instanceof RemoveEnemies) {
            EnemyHandler.INSTANCE.removeAllEnemies();

        } else if (object instanceof LobbyResponse) {
            LobbyResponse lobbyResponse = (LobbyResponse) object;
            LobbyHandler.INSTANCE.addUser(lobbyResponse.users);

        } else if (object instanceof LobbyUserUpdate) {
            LobbyUserUpdate lobbyUserUpdate = (LobbyUserUpdate) object;
            LobbyHandler.INSTANCE.deleteUsers(lobbyUserUpdate.userName);

        } else if (object instanceof GameStart) {
            LobbyHandler.INSTANCE.startGame();

        } else if (object instanceof CurrentState) {
            CurrentState currentState = (CurrentState) object;
            LobbyHandler.INSTANCE.setCurrentState(currentState.gameStatus);

        } else if (object instanceof DestructibleWalls) {
            DestructibleWalls destructibleWalls = (DestructibleWalls) object;
            if (destructibleWalls.destructibleWalls.size() > 0 && GameState.INSTANCE.getDestructibleWalls().size() == 0) {
                World.INSTANCE.setServerMap(destructibleWalls.destructibleWalls);
            } else {
                World.INSTANCE.setMap();
            }

        } else if (object instanceof RemovePowerUps) {
            RemovePowerUps removePowerUps = (RemovePowerUps) object;
            BombHandler.INSTANCE.removeAnotherPlayerPowerUps(removePowerUps.powerUpId);

        }
        super.received(connection, object);
    }
}
