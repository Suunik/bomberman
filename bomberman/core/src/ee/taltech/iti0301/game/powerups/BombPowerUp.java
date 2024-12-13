package ee.taltech.iti0301.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.textures.BombermanTexture;

public class BombPowerUp extends PowerUp {
    /**
     * bomb power up has bomb power ups sprite and gives character max bomb amount stat
     * @param position of the power up
     */
    public BombPowerUp(Vector2 position) {
        super(position);
        Texture bombPowerUpTexture = BombermanTexture.getBombPowerUpTexture();
        setSprite(bombPowerUpTexture, position);
    }
}

