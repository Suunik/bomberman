package ee.taltech.iti0301.game.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ee.taltech.iti0301.textures.BombermanTexture;

public class DeathAnimation {
    /**
     * Class holds and returns death animation
     */
    private final float spriteHeight;
    private final float spriteWidth;
    private Animation deathAnimation;
    private float animationTimer = 0;
    private float animationDuration = 2;
    private final Vector2 position;

    public DeathAnimation(Vector2 position, float height, float width) {
        this.spriteHeight = height;
        this.spriteWidth = width;
        this.position = position;
        createAnimation();
    }

    /**
     * creates the animation
     */
    private void createAnimation() {
        Texture characterTexture = BombermanTexture.getBomberManSheet();

        Array<Sprite> deathSprites = new Array<>();

        final int spriteStep = 33;
        final int frames = 4;
        final int posY = 225;
        for (int index = 0; index < frames; ++index) {
            int posX = index * spriteStep;
            Sprite deathSprite = new Sprite(characterTexture, posX, posY, 16, 22);
            deathSprite.setSize(spriteWidth, spriteHeight);
            deathSprites.add(deathSprite);
        }

        deathAnimation = new Animation<>(animationDuration / frames, deathSprites);
    }

    /**
     * gets the current animation sprite using animation timer variable
     * @return
     */
    public Sprite getFrame() {
        Sprite frame = (Sprite) deathAnimation.getKeyFrame(animationTimer, false);
        frame.setPosition(position.x, position.y);

        animationTimer += Gdx.graphics.getDeltaTime();
        return frame;
    }

    /**
     * gets the first frame of the animation
     * @return first frame of the animation
     */
    public Sprite getFirstFrame() {
        Sprite frame = (Sprite) deathAnimation.getKeyFrame(0);
        frame.setPosition(position.x, position.y);

        return frame;
    }

    public float getAnimationDuration() {
        return animationDuration;
    }

    public float getAnimationTimer() {
        return animationTimer;
    }
}
