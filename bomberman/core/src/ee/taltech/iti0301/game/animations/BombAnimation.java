package ee.taltech.iti0301.game.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ee.taltech.iti0301.textures.BombermanTexture;

public class BombAnimation {
    /**
     * Class holds bombs animation.
     * Animation is created in constructor
     */
    private final float spriteHeight;
    private final float spriteWidth;
    private Animation bombAnimation;
    private final float bombTimer;
    private float animationTimer = 0;
    private final Vector2 position;

    public BombAnimation(Vector2 position, float height, float width, float bombTimer) {
        this.spriteHeight = height;
        this.spriteWidth = width;
        this.position = position;
        this.bombTimer = bombTimer;
        createAnimation();
    }

    /**
     * creates the animation for bomb
     */
    private void createAnimation() {
        Texture bombTexture = BombermanTexture.getBombSheet();

        Array<Sprite> bombSprites = new Array<>();

        int spriteStep = 20;
        int frames = 3;

        for (int index = 0; index < frames; ++index) {
            int posX = index * spriteStep;
            Sprite bombSprite = new Sprite(bombTexture, posX, 2, 22, 22);
            bombSprite.setSize(spriteWidth, spriteHeight);
            bombSprites.add(bombSprite);
        }

        bombAnimation = new Animation<>(bombTimer / frames, bombSprites);
    }

    /**
     * gets the correct sprite from animation using animationTimer variable
     * updates animationTimer with delta time
     * @return frame
     */
    public Sprite getFrame() {
        Sprite frame = (Sprite) bombAnimation.getKeyFrame(animationTimer, false);
        frame.setPosition(position.x, position.y);

        animationTimer += Gdx.graphics.getDeltaTime();
        return frame;
    }
}
