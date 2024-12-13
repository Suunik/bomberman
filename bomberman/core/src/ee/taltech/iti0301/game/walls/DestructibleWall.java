package ee.taltech.iti0301.game.walls;

import com.badlogic.gdx.math.Rectangle;
import ee.taltech.iti0301.game.map.World;
import ee.taltech.iti0301.game.powerups.PowerUp;
import ee.taltech.iti0301.game.powerups.PowerUpFactory;
import ee.taltech.iti0301.game.powerups.PowerUpType;
import ee.taltech.iti0301.textures.BombermanTexture;

import java.util.Optional;
import java.util.Random;

public class DestructibleWall extends Wall {
    private final double powerUpChance = 0.25;
    private PowerUp powerUp;

    public DestructibleWall(Rectangle rectangle, Integer wallNum) {
        super(rectangle);
        setSprite(BombermanTexture.getDestructibleTileTexture());
        if (wallNum > -1) {
            Optional<PowerUp> powerUp = assignPowerUpFromList(wallNum);
            powerUp.ifPresent(power -> this.powerUp = power);
        }
    }

    public DestructibleWall(Rectangle rectangle) {
        super(rectangle);
        setSprite(BombermanTexture.getDestructibleTileTexture());
        if (!isHoldingPowerUp()) {
            World.INSTANCE.addDestructibleWalls(-1);
            return;
        }
        Optional<PowerUp> powerUp = assignPowerUp();
        powerUp.ifPresent(power -> this.powerUp = power);
    }
    private boolean isHoldingPowerUp() {
        return new Random().nextDouble() < powerUpChance;
    }

    /**
     * assigns power up to the wall at random
     * @return power up that was assigned (or optional.empty() if it wasn't assigned)
     */
    private Optional<PowerUp> assignPowerUp() {
        int numberOfDifferentPowerUps = PowerUpType.values().length;
        int randomNumber = new Random().nextInt(0, numberOfDifferentPowerUps);
        World.INSTANCE.addDestructibleWalls(randomNumber);
        PowerUpType random = PowerUpType.values()[randomNumber];
        return PowerUpFactory.createPowerUp(random, getPosition());
    }

    /**
     * assigns power up to the wall using power up type number
     * @param wallNum number of the power up
     * @return optional power up
     */
    private Optional<PowerUp> assignPowerUpFromList(Integer wallNum) {
        PowerUpType powerUp = PowerUpType.values()[wallNum];
        return PowerUpFactory.createPowerUp(powerUp, getPosition());
    }

    public Optional<PowerUp> getPowerUp() {
        return Optional.ofNullable(powerUp);
    }
}
