package ee.taltech.iti0301.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.textures.BombermanTexture;

public class ExplosionPowerUp extends PowerUp{
    /**
     * explosion power up has explosion power ups sprite and gives character explosion size stat
     * @param position of the power up
     */
    public ExplosionPowerUp(Vector2 position) {
        super(position);
        Texture explosionPowerUpTexture = BombermanTexture.getExplosionPowerUpTexture();
        setSprite(explosionPowerUpTexture, position);
    }
}

