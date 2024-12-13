package ee.taltech.iti0301.network.handlers;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.game.BombermanGdxGame;
import ee.taltech.iti0301.game.character.Ai;
import ee.taltech.iti0301.game.gameState.GameState;
import ee.taltech.iti0301.network.events.AddEnemy;
import ee.taltech.iti0301.network.events.EnemyMoveUpdate;
import ee.taltech.iti0301.network.events.RemoveEnemies;

import java.util.LinkedList;
import java.util.Set;

public class EnemyHandler {

    public static final EnemyHandler INSTANCE = new EnemyHandler();
    private final LinkedList<Ai> enemies;
    private Vector2 position;

    public EnemyHandler() {
        this.enemies = new LinkedList<>();
    }

    public Ai createNewEnemy() {
        AddEnemy addEnemy = new AddEnemy();
        Ai ai = new Ai();
        enemies.add(ai);
        addEnemy.x = 990;
        addEnemy.y = 100;
        BombermanGdxGame.client.sendUDP(addEnemy);
        return ai;
    }

    public void removeCollidedEnemies(final Set<Ai> enemySet) {
        enemies.removeAll(enemySet);
        GameState.INSTANCE.removeAllEnemies(enemySet);
        if (enemySet.size() != 0) {
            RemoveEnemies removeEnemies = new RemoveEnemies();
            BombermanGdxGame.client.sendUDP(removeEnemies);
        }
    }

    public void setEnemyPosition(float x, float y) {
        position = new Vector2(x, y);
    }

    public LinkedList<Ai> getEnemies() {
        return enemies;
    }

    public void movement(boolean w, boolean a, boolean s, boolean d) {
        EnemyMoveUpdate enemyMoveUpdate = new EnemyMoveUpdate();
        enemyMoveUpdate.up = w;
        enemyMoveUpdate.left = a;
        enemyMoveUpdate.down = s;
        enemyMoveUpdate.right = d;
        BombermanGdxGame.client.sendTCP(enemyMoveUpdate);
    }

    public void removeAllEnemies() {
        enemies.clear();
        GameState.INSTANCE.emptyEnemyList();
    }
}
