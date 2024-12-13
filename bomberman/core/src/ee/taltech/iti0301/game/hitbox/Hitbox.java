package ee.taltech.iti0301.game.hitbox;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Hitbox {
    private Rectangle hitbox = new Rectangle();
    private final float height;
    private final float width;
    public Hitbox(Vector2 position, float height, float width) {
        this.height = height;
        this.width = width;

        setHitbox(position);
    }

    /**
     * sets the hitbox to the given position
      * @param position of the hitbox
     */
    public void setHitbox(Vector2 position) {
        float posX = position.x;
        float posY = position.y;

        float hitboxCorrectionX = 7;
        float hitboxCorrectionY = 3;

        hitbox.set(posX + hitboxCorrectionX, posY,
                width - hitboxCorrectionX,
                height - hitboxCorrectionY);
    }

    /**
     * returns true if hitbox overlaps with other hitbox
     * @param anotherHitbox to check the collision on
     * @return true if collided, false otherwise
     */
    public boolean overlaps(Hitbox anotherHitbox) {
        return hitbox.overlaps(anotherHitbox.getHitbox());
    }

    /**
     *
     * @param rectangle to check the collision with
     * @return true if rectangle collides with hitbox, false otherwise
     */
    public boolean overlaps(Rectangle rectangle) {
        return hitbox.overlaps(rectangle);
    }
    public Rectangle getHitbox() {
        return hitbox;
    }
    public Vector2 getPosition() {
        return new Vector2(hitbox.getX(), hitbox.getY());
    }
    public void setPosition(Vector2 position) {
        hitbox.setPosition(position);
    }
    public void setSize(float width, float height) {
        hitbox.setSize(width, height);
    }
}

