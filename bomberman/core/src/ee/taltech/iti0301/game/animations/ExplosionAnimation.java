package ee.taltech.iti0301.game.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ee.taltech.iti0301.textures.BombermanTexture;

public class ExplosionAnimation {
    /**
     * Class holds and returns explosion animation
     */
    private final float spriteHeight;
    private final float spriteWidth;
    private Animation explosionEndAnimation;
    private Animation explosionMiddleAnimation;
    private Animation explosionCentreAnimation;
    private final float explosionTimer;
    private float animationTimer = 0;
    private final Vector2 position;

    public ExplosionAnimation (Vector2 position, float height, float width, float explosionTimer) {
        this.spriteHeight = height;
        this.spriteWidth = width;
        this.position = position;
        this.explosionTimer = explosionTimer;
        createAnimation();
    }

    /**
     * create the explosion animation using sprites from the explosion texture
     * animation is made with three parts rotated later depending on the direction of the explosion element
     * duration of the explosion animation is calculated by dividing frames by explosion timer (given in constructor)
     */
    private void createAnimation() {
        Texture explosionTexture = BombermanTexture.getExplosionTexture();

        Array<Sprite> explosionEndSprites = new Array<>();
        Array<Sprite> explosionMiddleSprites = new Array<>();
        Array<Sprite> explosionCentreSprites = new Array<>();

        int width = 15;
        int frames = 4;

        explosionEndSprites.add(new Sprite(explosionTexture, 13, 47, width, 16));
        explosionEndSprites.add(new Sprite(explosionTexture, 126, 49, width, 16));
        explosionEndSprites.add(new Sprite(explosionTexture, 244, 52, width, 16));
        explosionEndSprites.add(new Sprite(explosionTexture, 365, 52, width, 16));

        explosionMiddleSprites.add(new Sprite(explosionTexture, 29, 47, width,16));
        explosionMiddleSprites.add(new Sprite(explosionTexture, 142, 49, width,16));
        explosionMiddleSprites.add(new Sprite(explosionTexture, 261, 52, width,16));
        explosionMiddleSprites.add(new Sprite(explosionTexture, 382, 52, width,16));

        explosionCentreSprites.add(new Sprite(explosionTexture, 46, 47, width,16));
        explosionCentreSprites.add(new Sprite(explosionTexture, 159, 49, width,16));
        explosionCentreSprites.add(new Sprite(explosionTexture, 278, 52, width,16));
        explosionCentreSprites.add(new Sprite(explosionTexture, 399, 52, width,16));

        for (int i = 0; i < frames; ++i) {
            explosionEndSprites.get(i).setSize(spriteWidth, spriteHeight);
            explosionMiddleSprites.get(i).setSize(spriteWidth, spriteHeight);
            explosionCentreSprites.get(i).setSize(spriteWidth, spriteHeight);
        }

        float frameDuration = explosionTimer / frames;
        explosionEndAnimation = new Animation<>(frameDuration, explosionEndSprites);
        explosionMiddleAnimation = new Animation<>(frameDuration, explosionMiddleSprites);
        explosionCentreAnimation = new Animation<>(frameDuration, explosionCentreSprites);
    }

    /**
     *
     * @return the correct frame for explosions end element
     */
    public Sprite getFrameEnd() {
        return (Sprite) explosionEndAnimation.getKeyFrame(animationTimer, false);
    }

    /**
     *
     * @return the correct frame for explosions middle element
     */
    public Sprite getFrameMiddle() {
        return (Sprite) explosionMiddleAnimation.getKeyFrame(animationTimer, false);
    }

    /**
     *
     * @return correct frame for explosions centre
     */
    public Sprite getFrameCentre() {
        Sprite frame = (Sprite) explosionCentreAnimation.getKeyFrame(animationTimer, false);
        frame.setPosition(position.x, position.y);

        return frame;
    }

    /**
     * updates the animation timer
     * @param dt delta time
     */
    public void updateAnimationTimer(float dt) {
        animationTimer += dt;
    }
}
