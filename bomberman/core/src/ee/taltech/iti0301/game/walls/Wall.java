package ee.taltech.iti0301.game.walls;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.game.hitbox.Hitbox;

public class Wall {
    protected final Hitbox hitbox;
    private Sprite sprite;
    private final float wallWidth = 60;
    private final float wallHeight = 60;
    private final float size = 1.25f;
    private final float locationCorrection = size;

    public Wall(Rectangle rectangle) {
        hitbox = new Hitbox(
                new Vector2(rectangle.getX() * locationCorrection, rectangle.getY() * locationCorrection),
                rectangle.getHeight(), rectangle.getWidth());
    }
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    /**
     * sets the position and size of the sprite
     * position is taken from the hitbox position
     * @param texture to get the sprite from
     */
    protected void setSprite(Texture texture) {
        this.sprite = new Sprite(texture, 1, 1, 60,60);
        setPosition(hitbox.getPosition());
        setSize();
    }
    public void setPosition(Vector2 position) {
        sprite.setPosition(position.x, position.y);
    }
    public void setSize() {
        sprite.setSize(wallWidth * size, wallHeight * size);
        hitbox.setSize(wallWidth * size, wallHeight * size);
    }
    public Vector2 getPosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }
    public Hitbox getHitbox() {
        return hitbox;
    }
}
