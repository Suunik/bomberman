package ee.taltech.iti0301.network.handlers;

import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.game.BombermanGdxGame;
import ee.taltech.iti0301.game.bomb.Bomb;
import ee.taltech.iti0301.game.character.Player;
import ee.taltech.iti0301.game.gameState.GameState;
import ee.taltech.iti0301.game.powerups.PowerUp;
import ee.taltech.iti0301.network.events.AddBomb;
import ee.taltech.iti0301.network.events.RemovePowerUps;

import java.util.HashSet;
import java.util.Set;

public class BombHandler {
    public static final BombHandler INSTANCE = new BombHandler();

    public Bomb createBomb(Vector2 position) {
        final AddBomb addBomb = new AddBomb();
        Player player =  PlayerHandler.INSTANCE.getPlayerByUsername(PlayerHandler.INSTANCE.getUsername());
        if (player == null) {
            return null;
        }
        Integer size = player.getExplosionSize();
        addBomb.x = position.x;
        addBomb.y = position.y;
        addBomb.size = size;
        BombermanGdxGame.client.sendTCP(addBomb);

        Bomb bomb = new Bomb(new Vector2(position.x, position.y), size);
        addBomb(bomb);
        return bomb;
    }

    public void addBomb(final Bomb bomb) {
        GameState.INSTANCE.addBomb(bomb);
    }

    public void removeBomb(Set<Bomb> bombsToRemove) {
        GameState.INSTANCE.removeAllBombs(bombsToRemove);
    }

    public void removePowerUps(Set<PowerUp> powerUpSet) {
        RemovePowerUps removePowerUps = new RemovePowerUps();
        Set<Integer> powerUpIds = new HashSet<>();
        for (PowerUp powerUp : powerUpSet) {
            powerUpIds.add(powerUp.getPowerUpId());
        }
        removePowerUps.powerUpId = powerUpIds;
        GameState.INSTANCE.getPowerUps().removeAll(powerUpSet);
        BombermanGdxGame.client.sendUDP(removePowerUps);
    }

    private PowerUp getPowerUpById(Integer id) {
        for (PowerUp powerUp : GameState.INSTANCE.getPowerUps()) {
            if (powerUp.getPowerUpId() == id) {
                return powerUp;
            }
        }
        return null;
    }

    public void removeAnotherPlayerPowerUps(Set<Integer> powerUpIds) {
        Set<PowerUp> powerUpsToRemove = new HashSet<>();
        for (Integer id : powerUpIds) {
            powerUpsToRemove.add(getPowerUpById(id));
        }
        GameState.INSTANCE.getPowerUps().removeAll(powerUpsToRemove);
    }
}
