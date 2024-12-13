package ee.taltech.iti0301.game.powerups;

import com.badlogic.gdx.math.Vector2;

import java.util.Optional;

public class PowerUpFactory {
    public static Optional<PowerUp> createPowerUp(PowerUpType type, Vector2 position) {
        switch (type) {
            case BOMB: return Optional.of(new BombPowerUp(position));
            case SPEED: return Optional.of(new SpeedPowerUp(position));
            case EXPLOSION: return Optional.of(new ExplosionPowerUp(position));
        }
        return Optional.empty();
    }
}
