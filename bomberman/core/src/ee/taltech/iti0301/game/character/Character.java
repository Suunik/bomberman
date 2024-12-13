package ee.taltech.iti0301.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.game.animations.CharacterAnimation;
import ee.taltech.iti0301.game.bomb.Bomb;
import ee.taltech.iti0301.game.gameState.GameState;
import ee.taltech.iti0301.game.hitbox.Hitbox;
import ee.taltech.iti0301.game.powerups.BombPowerUp;
import ee.taltech.iti0301.game.powerups.ExplosionPowerUp;
import ee.taltech.iti0301.game.powerups.PowerUp;
import ee.taltech.iti0301.game.powerups.SpeedPowerUp;
import ee.taltech.iti0301.network.handlers.BombHandler;
import ee.taltech.iti0301.network.handlers.EnemyHandler;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Character {

    private final String username;
    protected enum MoveDirections {UP, DOWN, LEFT, RIGHT}
    // SPRITES AND ANIMATION
    protected final CharacterAnimation animation;
    private Sprite characterSprite;
    protected GameState gameState = new GameState();
    //HITBOX AND SIZE
    private final Hitbox hitbox;
    private Hitbox allowedCollidable;
    private final float characterSize = 1.2f;
    private final float characterWidth = 42.5f * characterSize;
    private final float characterHeight = 60 * characterSize;
    // CHARACTER STATS
    protected int movementSpeed = 200;
    private int maxBombsAmount = 1;
    private Set<Bomb> bombsDropped = new HashSet<>();
    private Integer explosionSize = 1;

    public Character(String username) {
        this.username = username;

        animation = new CharacterAnimation(characterWidth, characterHeight);
        characterSprite = animation.getCurrentSprite();

        hitbox = new Hitbox(getPosition(), characterHeight, characterWidth);
    }

    /**
     * updates the characters sprite and renders it on the batch, updates allowed collidable
     * (collidable that can be walked over)
     * @param batch
     * @param gameState
     */
    public void update(SpriteBatch batch, GameState gameState) {
        render(batch);
        this.gameState = gameState;
        allowedCollidable = getAllowedCollidable();
        // if character has dropped bomb we must check if character has left the bomb area so the bomb hitbox can
        // become active for the character
        checkForBombCollisionAfterDrop();
    }

    /**
     * set the character sprite using animation and render the character on given batch
     * @param batch to draw the sprite onto
     */
    protected void render(SpriteBatch batch) {
        setSprite(animation.getFrame(getPosition()));
        drawCharacter(batch);
    }

    /**
     * sets the sprite and sprites position
     * @param spriteToSet set the given sprite as characters sprite
     */
    protected void setSprite(Sprite spriteToSet) {
        // set the sprite and position of the sprite
        characterSprite = spriteToSet;
        characterSprite.setPosition(getPosition().x, getPosition().y);
    }

    /**
     * draws character on the given sprite batch
     * @param batch to draw the character onto
     */
    protected void drawCharacter(SpriteBatch batch) {
        characterSprite.draw(batch);
    }

    /**
     * tries to move the character to the position given
     * returns character to previous position if next position would collide with a collidable
     * @param moveDirection to try and move the character to
     */
    // MOVEMENT AND ACTIONS
    protected void movement(MoveDirections moveDirection) {
        // remember the position at the start of the frame, so we can return it if collision was found
        float previousX = characterSprite.getX();
        float previousY = characterSprite.getY();
        boolean w = false, a = false, s = false, d = false;
        // move the character sprite to the direction given in arguments
        if (moveDirection == MoveDirections.UP) {
            w = true;
            characterSprite.setY(characterSprite.getY() + movementSpeed * Gdx.graphics.getDeltaTime());

        } else if (moveDirection == MoveDirections.LEFT) {
            a = true;
            characterSprite.setX(characterSprite.getX() - movementSpeed * Gdx.graphics.getDeltaTime());

        } else if (moveDirection == MoveDirections.DOWN) {
            s = true;
            characterSprite.setY(characterSprite.getY() - movementSpeed * Gdx.graphics.getDeltaTime());

        } else if (moveDirection == MoveDirections.RIGHT) {
            d = true;
            characterSprite.setX(characterSprite.getX() + movementSpeed * Gdx.graphics.getDeltaTime());
        }
        EnemyHandler.INSTANCE.movement(w, a, s, d);

        // set the hitbox position
        hitbox.setHitbox(getPosition());
        // check for collision
        // if collision was found we must return the position we were at the start of the frame
        if (checkForCollision(gameState.getCollidableHitBoxes())) {
            characterSprite.setX(previousX);
            characterSprite.setY(previousY);
            // set the hitbox position
            hitbox.setHitbox(getPosition());
        }
        //check if character has collided with a power up item
        checkForPowerUpCollision();
    }

    /**
     * drops the bomb if allowed by the max bombs amount
     * cannot drop bomb if character is positioned on the allowed collidable (positioned on another bomb)
     */
    protected void dropBomb() {

        // check if player has not left the previous bomb drop area
        if (allowedCollidable != null) {
            return;
        }
        bombsDropped = updateBombsDropped();
        if (bombsDropped.size() >= maxBombsAmount) {
            return;
        }

        Bomb bomb = BombHandler.INSTANCE.createBomb(new Vector2(characterSprite.getX(), characterSprite.getY()));
        bombsDropped.add(bomb);
    }

    /**
     * gets all the bombs in game state
     * @return all the bombs in game state
     */
    private Set<Bomb> updateBombsDropped() {
        return bombsDropped.stream().filter(bomb -> gameState.getBombs().contains(bomb)).collect(Collectors.toSet());
    }

    /**
     * check for collision with another hitbox in the list given
     * @param hitboxes to look for the collision from
     * @return true if collided, false otherwise
     */
    // COLLISION
    private boolean checkForCollision(Set<Hitbox> hitboxes) {
        for (Hitbox hitbox: hitboxes) {
            if (allowedCollidable != null && hitbox.getPosition().equals(allowedCollidable.getPosition())) {
                continue;
            }
            if (this.hitbox.overlaps(hitbox)) {
                return true;
            }
        }
        return false;
    }

    /**
     * find a collided power up and update corresponding stat
     */
    public void checkForPowerUpCollision() {
        Set<PowerUp> powerUpsToRemove = new HashSet<>();
        for (PowerUp powerUp : gameState.getPowerUps()) {
            if (hitbox.overlaps(powerUp.getHitbox())) {

                checkForExplosionPowerUp(powerUp);
                checkForBombPowerUp(powerUp);
                checkForSpeedPowerUp(powerUp);

                powerUpsToRemove.add(powerUp);
            }
        }
        BombHandler.INSTANCE.removePowerUps(powerUpsToRemove);
    }

    /**
     * updates characters explosion size of given power up is explosion size power up
     * @param powerUp to check
     */
    private void checkForExplosionPowerUp(PowerUp powerUp) {
        if (powerUp instanceof ExplosionPowerUp) {
            explosionSize += 1;
        }
    }

    /**
     * updates characters speed if given power up is speed power up
     * @param powerUp to check
     */
    private void checkForSpeedPowerUp(PowerUp powerUp) {
        if (powerUp instanceof SpeedPowerUp) {
            movementSpeed += 50;
        }
    }

    /**
     * updates characters max bombs amount if given power up is explosion size power up
     * @param powerUp to check
     */
    private void checkForBombPowerUp(PowerUp powerUp) {
        if (powerUp instanceof BombPowerUp) {
            maxBombsAmount += 1;
        }
    }

    /**
     * if player first leaves the dropped bombs area, allowed collidable variable will become null
     * so that player cant enter again
     */
    private void checkForBombCollisionAfterDrop() {
        if (allowedCollidable == null) {
            return;
        }
        if (!hitbox.overlaps(allowedCollidable)) {
            allowedCollidable = null;
        }
    }

    /**
     * sets the character position and hitbox to given X and Y coordinates
     * @param posX coordinate
     * @param posY coordinate
     */
    // GETTERS AND SETTERS
    public void setCharacterPosition(float posX, float posY) {
        characterSprite.setPosition(posX, posY);
        hitbox.setHitbox(getPosition());
    }

    public Vector2 getPosition() {
        return new Vector2(characterSprite.getX(), characterSprite.getY());
    }
    public Hitbox getHitbox() {
        return hitbox;
    }

    protected Vector2 getCharacterSize() {
        return new Vector2(characterWidth * characterSize, characterHeight * characterSize);
    }

    /**
     * if any bombs hitbox collides with characters hitbox, means that the bomb was dropped on top of the player
     * @return the collided hitbox
     */
    private Hitbox getAllowedCollidable() {
        Optional<Hitbox> collidedHitbox = this.gameState.getBombs().stream().map(Bomb::getHitBox)
                .filter(collidableHitbox -> collidableHitbox.overlaps(this.hitbox)).findFirst();

        return collidedHitbox.orElse(null);
    }
    public Integer getExplosionSize() {
        return explosionSize;
    }

    public float getCharacterWidth() {
        return characterWidth;
    }

    public float getCharacterHeight() {
        return characterHeight;
    }
    public String getUsername() {
        return username;
    }
}
