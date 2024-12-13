package ee.taltech.bomberman.server.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.bomberman.server.GameServer;
import ee.taltech.bomberman.server.character.Enemy;
import ee.taltech.bomberman.server.character.Player;
import ee.taltech.bomberman.server.handlers.EnemyHandler;
import ee.taltech.bomberman.server.handlers.LobbyHandler;
import ee.taltech.bomberman.server.handlers.PlayerHandler;
import ee.taltech.bomberman.server.handlers.World;
import ee.taltech.iti0301.network.events.*;

public class EventListener extends Listener {
    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);

        if (object instanceof PlayerUpdate) {
            final PlayerUpdate playerUpdate = (PlayerUpdate) object;
            if (PlayerHandler.INSTANCE.getPlayerByUsername(playerUpdate.username) == null) return;
            final Player player = PlayerHandler.INSTANCE.getPlayerByConnection(connection);
            player.setX(playerUpdate.x);
            player.setY(playerUpdate.y);

        } else if (object instanceof AddPlayer) {
            AddPlayer addPlayer = (AddPlayer) object;
            final Player player = new Player(addPlayer.username, connection);
            player.setX(addPlayer.x);
            player.setY(addPlayer.y);
            if (PlayerHandler.INSTANCE.getPlayerByUsername(((AddPlayer) object).username) != null) {
                return;
            }
            PlayerHandler.INSTANCE.addPlayer(player);

        } else if (object instanceof AddBomb) {
            final AddBomb addBomb = (AddBomb) object;
            GameServer.server.sendToAllTCP(addBomb);

        } else if (object instanceof RemoveCollidedPlayers) {
            final RemoveCollidedPlayers removeCollidedPlayers = (RemoveCollidedPlayers) object;
            for (String name : removeCollidedPlayers.names) {
                PlayerHandler.INSTANCE.removePlayer(PlayerHandler.INSTANCE.getPlayerByUsername(name));
            }

        } else if (object instanceof AddEnemy) {
            AddEnemy addEnemy = (AddEnemy) object;
            final Enemy enemy = new Enemy(connection);
            enemy.setX(addEnemy.x);
            enemy.setY(addEnemy.y);
            if (EnemyHandler.INSTANCE.getEnemies().size() < 1) {
                EnemyHandler.INSTANCE.addEnemy(enemy);
            }

        } else if (object instanceof EnemyMoveUpdate) {
            EnemyMoveUpdate enemyMoveUpdate = (EnemyMoveUpdate) object;
            if (EnemyHandler.INSTANCE.getEnemyByConnection(connection) == null) return;
            Enemy enemy = EnemyHandler.INSTANCE.getEnemyByConnection(connection);
            enemy.setW(enemyMoveUpdate.up);
            enemy.setA(enemyMoveUpdate.left);
            enemy.setS(enemyMoveUpdate.down);
            enemy.setD(enemyMoveUpdate.right);

        } else if (object instanceof RemoveEnemies) {
            EnemyHandler.INSTANCE.removeEnemies();

        } else if (object instanceof GameStart) {
            LobbyHandler.INSTANCE.startGame(connection.getID());

        } else if (object instanceof CurrentState) {
            CurrentState currentState = (CurrentState) object;
            if (currentState.gameStatus) World.INSTANCE.removeWalls();
            LobbyHandler.INSTANCE.setStatus(currentState.gameStatus);
        }

        else if (object instanceof DestructibleWalls) {
            DestructibleWalls destructibleWalls = (DestructibleWalls) object;
            World.INSTANCE.setDestructibleWalls(destructibleWalls.destructibleWalls);

        } else if (object instanceof RemovePowerUps) {
            RemovePowerUps removePowerUps = (RemovePowerUps) object;
            GameServer.server.sendToAllExceptUDP(connection.getID(), removePowerUps);

        } else if (object instanceof RequestMap) {
            DestructibleWalls destructibleWalls = new DestructibleWalls();
            destructibleWalls.destructibleWalls = World.INSTANCE.getDestructibleWalls();
            GameServer.server.sendToTCP(connection.getID(), destructibleWalls);
        }
    }
}
