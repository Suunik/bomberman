package ee.taltech.iti0301.game.character;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.game.animations.DeathAnimation;

public class DeadCharacter {
    /**
     * dead character is uncontrollable character that plays out death animation on the batch given
     */
    private final DeathAnimation deathAnimation;
    private Sprite sprite;
    public DeadCharacter(Vector2 position, float width, float height) {
        deathAnimation = new DeathAnimation(position, height, width);
        sprite = deathAnimation.getFirstFrame();
    }

    /**
     * update and draw the spite onto sprite batch
     * @param batch to draw the sprite onto
     */
    public void update(SpriteBatch batch) {
        sprite = deathAnimation.getFrame();
        sprite.draw(batch);
    }

    /**
     *
     * @return true if animation has played it's course so that the instance of dead character can be removed
     */
    public boolean animationIsDone() {
        return deathAnimation.getAnimationDuration() <= deathAnimation.getAnimationTimer();
    }
}
