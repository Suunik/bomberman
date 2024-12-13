package ee.taltech.iti0301.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;


public class Player extends Character {
    public Player(String username) {
        super(username);
    }

    /**
     * moves the player character according to the key pressed
     */
    public void inputMovement() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            movement(MoveDirections.UP);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            movement(MoveDirections.DOWN);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            movement(MoveDirections.LEFT);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            movement(MoveDirections.RIGHT);
        }
    }

    /**
     * drops the bomb when correct key is pressed
     */
    public void inputDropBomb() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            dropBomb();
        }
    }


}

