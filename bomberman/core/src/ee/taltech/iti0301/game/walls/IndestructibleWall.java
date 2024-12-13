package ee.taltech.iti0301.game.walls;

import com.badlogic.gdx.math.Rectangle;
import ee.taltech.iti0301.textures.BombermanTexture;

public class IndestructibleWall extends Wall {
    public IndestructibleWall(Rectangle rectangle) {
        super(rectangle);
        setSprite(BombermanTexture.getIndestructibleTileTexture());
    }
}
