package ee.taltech.iti0301.game.bomb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.game.animations.BombAnimation;
import ee.taltech.iti0301.game.hitbox.Hitbox;
import ee.taltech.iti0301.textures.BombermanTexture;

public class Bomb {
    private Sprite bombSprite;
    private final Hitbox hitbox;
    private final float bombSize = 1.25f;
    private float timeUntilExplosion = 3;
    private boolean exploded = false;
    private final float bombWidth = 60 * bombSize;
    private final float bombHeight = 60 * bombSize;
    private final Integer explosionSize;
    private final BombAnimation animation;
    private final Explosion explosion;

    /**
     * bomb class constructor
     * @param position of the bomb
     * @param explosionSize - size of the explosion this bomb will create
     */
    public Bomb(Vector2 position, Integer explosionSize) {
        setSprite(position);
        this.explosionSize = explosionSize;
        hitbox = new Hitbox(position,
                bombHeight, bombWidth);
        explosion = new Explosion(this.explosionSize, position);
        animation = new BombAnimation(getPosition(), bombHeight, bombWidth, timeUntilExplosion);
    }

    /**
     * updates the bombs sprite and draws it to the SpriteBatch given
     * also updates the bombs timer
     * @param batch where bomb is drawn
     */
    public void update(SpriteBatch batch) {
        bombSprite = animation.getFrame();
        drawBomb(batch);
        updateBombTimer();
    }

    /**
     * draw the bomb into sprite batch given
     * @param batch to draw the bomb onto
     */
    public void drawBomb(SpriteBatch batch) {
        bombSprite.draw(batch);
    }

    /**
     *
     * @return bombs hitbox
     */
    public Hitbox getHitBox() {
        return hitbox;
    }

    /**
     * updates the bomb timer using delta time
     */
    private void updateBombTimer() {
        float dt = Gdx.graphics.getDeltaTime();
        timeUntilExplosion -= dt;

        if (timeUntilExplosion <= 0) {
            exploded = true;
        }
    }

    /**
     * set the sprite of the bomb on the position given in parameters
     * sets the bombs position according to the map "grid"
     * @param position where to set the bombs sprite
     */
    private void setSprite(Vector2 position) {
        Texture bombSheet = BombermanTexture.getBombSheet();
        bombSprite = new Sprite(bombSheet, 0, 4, 20, 20);
        bombSprite.setSize(bombWidth, bombHeight);
        // sets the position according to the map "grid"
        position.x = Math.round(position.x / bombWidth) * bombWidth + 4;
        position.y = Math.round(position.y / bombWidth) * bombHeight;
        bombSprite.setPosition(position.x, position.y);
    }

    /**
     * exploded is set to true if bombs timer runs out
     * @return if bomb has exploded
     */
    public boolean shouldExplode() {
        return exploded;
    }
    public Vector2 getPosition() {
        return new Vector2(bombSprite.getX(), bombSprite.getY());
    }

    /**
     *
     * @return bombs explosion hitbox vertical area
     */
    public Rectangle bombExplosionAreaVertical() {
        return explosion.getHitboxVertical().getHitbox();
    }

    /**
     *
     * @return bombs explosion hitbox horizontal area
     */
    public Rectangle bombExplosionAreaHorizontal() {
        return explosion.getHitboxHorizontal().getHitbox();
    }

    /**
     *
     * @return bombs explosion
     */
    public Explosion getExplosion() {
        return explosion;
    }
    public void setExploded(boolean explodedState) {
        exploded = explodedState;
    }
}
