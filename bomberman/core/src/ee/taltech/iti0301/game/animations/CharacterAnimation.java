package ee.taltech.iti0301.game.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ee.taltech.iti0301.textures.BombermanTexture;

public class CharacterAnimation {
    /**
     * Class holds character animation.
     * Animation is created in constructor
     */
    private enum CharacterState {
        FACING_UP, FACING_LEFT, FACING_DOWN, FACING_RIGHT,
        RUNNING_UP, RUNNING_LEFT, RUNNING_DOWN, RUNNING_RIGHT}
    private final float characterWidth;
    private final float characterHeight;
    private Sprite currentSprite;
    private CharacterState currentState = CharacterState.FACING_DOWN;
    private CharacterState previousState = CharacterState.FACING_DOWN;
    private Vector2 previousPosition = new Vector2(0,0);
    private final float animationSpeed = 0.5f;
    private Animation runUpAnimation;
    private Animation runDownAnimation;
    private Animation runLeftAnimation;
    private Animation runRightAnimation;
    private Sprite faceUp;
    private Sprite faceLeft;
    private Sprite faceDown;
    private Sprite faceRight;
    private float stateTimer = 0;

    public CharacterAnimation(float characterWidth, float characterHeight) {
        this.characterWidth = characterWidth;
        this.characterHeight = characterHeight;
        createAnimation();
        currentSprite = faceDown;
    }

    /**
     * Get the correct sprite for the character using characters position.
     * Finds out the direction character has moved since the last frame and returns corresponding sprite
     * updates animationTimer, previous position and current state using the position given in parameters
     * @param position to compare previous position against
     * @return sprite
     */
    public Sprite getFrame(Vector2 position) {
        // returns current sprite if nothing happens
        Sprite frame = currentSprite;
        currentState = assignState(position);
        // if the character has not moved since the last frame, means it is standing still
        // set the state to facing x direction depending on the direction character was running before

        // check the state of the character and give corresponding keyframe to it
        switch (currentState) {
            case RUNNING_UP:
                frame = (Sprite) runUpAnimation.getKeyFrame(stateTimer, true);
                break;
            case RUNNING_LEFT:
                frame = (Sprite) runLeftAnimation.getKeyFrame(stateTimer, true);
                break;
            case RUNNING_DOWN:
                frame = (Sprite) runDownAnimation.getKeyFrame(stateTimer, true);
                break;
            case RUNNING_RIGHT:
                frame = (Sprite) runRightAnimation.getKeyFrame(stateTimer, true);
                break;
            case FACING_UP:
                frame = faceUp;
                break;
            case FACING_LEFT:
                frame = faceLeft;
                break;
            case FACING_DOWN:
                frame = faceDown;
                break;
            case FACING_RIGHT:
                frame = faceRight;
                break;
        }

        // if the state of the character is same than the last frame, means that it is doing the same activity
        // means that we can add to the state timer, so it can get another frame else return it to 0
        // so that the next animation can start at the beginning.
        stateTimer = currentState == previousState ? stateTimer + Gdx.graphics.getDeltaTime() : 0;

        // remember the state and position of this frame
        previousState = currentState;
        previousPosition = position;
        currentSprite = frame;
        frame.setPosition(position.x, position.y);
        return frame;
    }

    /**
     * creates animation for all four directions character can move
     * also sets the "standing still" sprite for all four directions
     */
    private void createAnimation() {
        Texture bomberManSheet = BombermanTexture.getBomberManSheet();
        int spriteStep = 32;

        Array<Sprite> moveUpSprites = new Array<>();
        Array<Sprite> moveDownSprites = new Array<>();
        Array<Sprite> moveLeftSprites = new Array<>();
        Array<Sprite> moveRightSprites = new Array<>();
        // loop three times to get the sprites from bomberManSheet texture
        for (int index = 0; index < 3; ++index) {
            int posX = index * spriteStep;

            //cut and add sprites to the list
            moveUpSprites.add(new Sprite(bomberManSheet, 1 + posX, 1, 17, 24));
            moveRightSprites.add(new Sprite(bomberManSheet, 1 + posX, 32, 17, 24));
            moveDownSprites.add(new Sprite(bomberManSheet, 1 + posX, 64, 17, 24));
            moveLeftSprites.add(new Sprite(bomberManSheet, 1 + posX, 96, 17, 24));

            //set a new size for the sprite size
            moveRightSprites.get(index).setSize(characterWidth, characterHeight);
            moveUpSprites.get(index).setSize(characterWidth, characterHeight);
            moveLeftSprites.get(index).setSize(characterWidth, characterHeight);
            moveDownSprites.get(index).setSize(characterWidth, characterHeight);
        }
        // add the "standing still" sprites
        faceUp = moveUpSprites.get(0);
        faceLeft = moveLeftSprites.get(0);
        faceDown = moveDownSprites.get(0);
        faceRight = moveRightSprites.get(0);
        // initialize animations
        runUpAnimation = new Animation<>(0.1f / animationSpeed, moveUpSprites);
        runDownAnimation = new Animation<>(0.1f / animationSpeed, moveDownSprites);
        runLeftAnimation = new Animation<>(0.1f/ animationSpeed, moveLeftSprites);
        runRightAnimation = new Animation<>(0.1f / animationSpeed, moveRightSprites);
    }

    /**
     * finds the character state using position given and comparing it against position given last frame
     * @param position to compare against
     * @return characters state
     */
    private CharacterState assignState(Vector2 position) {
        if (previousPosition.equals(position)) {
            switch(previousState) {
                case RUNNING_UP:
                    return CharacterState.FACING_UP;
                case RUNNING_LEFT:
                    return CharacterState.FACING_LEFT;
                case RUNNING_DOWN:
                    return CharacterState.FACING_DOWN;
                case RUNNING_RIGHT:
                    return CharacterState.FACING_RIGHT;
            }
        }
        if (position.y - previousPosition.y < 0) {
            return CharacterState.RUNNING_DOWN;
        }
        if (position.y - previousPosition.y > 0) {
            return CharacterState.RUNNING_UP;
        }
        if (position.x - previousPosition.x < 0) {
            return CharacterState.RUNNING_LEFT;
        }
        if (position.x - previousPosition.x > 0) {
            return CharacterState.RUNNING_RIGHT;
        }
        return currentState;
    }
    public Sprite getCurrentSprite() {
        return currentSprite;
    }
}
