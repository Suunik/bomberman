package ee.taltech.iti0301.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.textures.BombermanTexture;

public class SpeedPowerUp extends PowerUp {
    /**
     * speed power up has speed power ups sprite and gives character speed stat
     * @param position of the power up
     */
    public SpeedPowerUp(Vector2 position) {
        super(position);
        Texture speedPowerUpTexture = BombermanTexture.getSpeedPowerUpTexture();
        setSprite(speedPowerUpTexture, position);
    }
}

