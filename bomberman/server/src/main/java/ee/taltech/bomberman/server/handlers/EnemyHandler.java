package ee.taltech.bomberman.server.handlers;

import com.esotericsoftware.kryonet.Connection;
import ee.taltech.bomberman.server.GameServer;
import ee.taltech.bomberman.server.character.Enemy;
import ee.taltech.iti0301.network.events.RemoveEnemies;

import java.util.LinkedList;

public class EnemyHandler {
    public static final EnemyHandler INSTANCE = new EnemyHandler();
    private final LinkedList<Enemy> enemies;

    public EnemyHandler() {
        this.enemies = new LinkedList<>();
    }

    public Enemy getEnemyByConnection(final Connection connection) {
        for (final Enemy enemy : enemies) {
            if (enemy.getConnection() == connection) {
                return enemy;
            }
        }
        return null;
    }

    public void addEnemy(final Enemy enemy) {
        enemies.add(enemy);
    }

    public LinkedList<Enemy> getEnemies() {
        return enemies;
    }

    public void removeEnemies() {
        enemies.clear();
        RemoveEnemies removeEnemies = new RemoveEnemies();
        GameServer.server.sendToAllUDP(removeEnemies);
    }

    public void removeAllEnemies() {
        enemies.clear();
    }
}
