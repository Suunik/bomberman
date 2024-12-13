package ee.taltech.iti0301.game.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.game.hitbox.Hitbox;

public class PowerUp {
    protected Sprite sprite;
    private final float powerUpSize = 1.25f;
    private final float powerUpHeight = 60 * powerUpSize;
    private final float powerUpWidth = 60 * powerUpSize;
    private final Hitbox hitbox;
    public static int id = 0;
    private int powerUpId;
    public static int newId() {
        id += 1;
        return id;
    }

    public static void resetIdCounter() {
        id = 0;
    }

    public PowerUp(Vector2 position) {
        hitbox = new Hitbox(position, powerUpHeight, powerUpWidth);
        powerUpId = newId();
    }
    public void update(SpriteBatch batch) {
        drawSprite(batch);
    }
    public void drawSprite(Batch batch) {
        sprite.draw(batch);
    }
    protected void setSprite(Texture texture, Vector2 position) {
        sprite = new Sprite(texture);
        sprite.setPosition(position.x, position.y);
        sprite.setSize(powerUpWidth, powerUpHeight);
    }
    public Hitbox getHitbox() {
        return hitbox;
    }
    public int getPowerUpId() {
        return powerUpId;
    }
}
