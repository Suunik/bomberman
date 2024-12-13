package ee.taltech.iti0301.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.game.walls.DestructibleWall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class Ai extends Character {
    private MoveDirections currentDirection = MoveDirections.UP;
    private Vector2 previousPosition = new Vector2();
    private float timeMovedInDirection = 0;

    private enum AIState {NORMAL, RUNNING_AWAY, NEAR_DESTRUCTIBLE_WALL, TOO_LONG_IN_SAME_DIRECTION, STAYING_STILL}
    private AIState currentState = AIState.NORMAL;
    private AIState previousState = AIState.NORMAL;
    private boolean hasServerDirection;
    public Ai() {
        super("AI");
    }
    @Override
    protected void render(SpriteBatch batch) {
        simpleAIActions();
        setSprite(animation.getFrame(getPosition()));
        drawCharacter(batch);
    }
    /**
     * move AI character and make it take actions
     */
    public void simpleAIActions() {
        setState();
        if (!hasServerDirection) {
            setDirection();
        }
        previousPosition = getPosition();
        movement(currentDirection);

        if (shouldDropBomb()) {
            dropBomb();
        }
    }

    public void setServerDirection(boolean w, boolean a, boolean s, boolean d) {
        hasServerDirection = true;
        if (w) {
            currentDirection = MoveDirections.UP;
        } else if (a) {
            currentDirection = MoveDirections.LEFT;
        } else if (s) {
            currentDirection = MoveDirections.DOWN;
        } else if (d) {
            currentDirection = MoveDirections.RIGHT;
        }
    }

    /**
     * decide if AI should drop a bomb this frame
     */
    public boolean shouldDropBomb() {
        if (currentState.equals(AIState.NEAR_DESTRUCTIBLE_WALL) && !previousState.equals(AIState.RUNNING_AWAY)) {
            currentState = AIState.NORMAL;
            return true;
        }
        return false;
    }

    /**
     * set the current state of the AI
     */
    private void setState() {
        if (!previousState.equals(currentState)) {
            previousState = currentState;
        }
        // potential position of the AI if kept moving in the current direction
        Vector2 potentialPosition = getPotentialPosition();
        // find out if potential position is safe or not
        Optional<Rectangle> dangerRectangle = findDangerHitbox(potentialPosition);

        if (dangerRectangle.isPresent()) { // this means that AI is about to run into explosion
            currentState = AIState.RUNNING_AWAY;
            return;
        }
        Optional<DestructibleWall> destructibleWall = findDestructibleWall(potentialPosition);
        if (destructibleWall.isPresent()) { // means that AI is near destructible wall
            currentState = AIState.NEAR_DESTRUCTIBLE_WALL;
            return;
        }

        if (previousPosition.equals(getPosition())) { // means that AI has not moved since last position
            currentState = AIState.STAYING_STILL;
            return;
        }

        // allowed time for AI to move in the same direction without anything happening
        float timeAllowedToMoveInDirection = 3;
        if (timeMovedInDirection >= timeAllowedToMoveInDirection) {
            currentState = AIState.TOO_LONG_IN_SAME_DIRECTION;
            timeMovedInDirection = 0;
            return;
        }

        timeMovedInDirection += Gdx.graphics.getDeltaTime();
        // if nothing else happens we can set the state to normal
        currentState = AIState.NORMAL;
    }
    /**
     * set a direction for the AI
     * Direction will change if AI has moved in the same direction for 3 seconds
     * when colliding with a wall
     * or when moving in the same direction will result in moving into explosion or explosion area.
     */
    private void setDirection() {
        switch (currentState) {
            case RUNNING_AWAY: { // if AI is running away for the first frame, change the direction
                setDirectionWhenRunningAway();
                break;
            }
            case STAYING_STILL:
            case TOO_LONG_IN_SAME_DIRECTION:
                currentDirection = getRandomDirectionExcept(currentDirection);
                break;
            case NORMAL:
                break;
        }
    }

    /**
     * finds the destructible wall that given position collides with
     * @param position to find the wall with
     * @return first found wall (if there is one)
     */
    private Optional<DestructibleWall> findDestructibleWall(Vector2 position) {
        //use the position to make a character sized rectangle
        Vector2 characterSize = getCharacterSize();
        Rectangle potentialHitbox = new Rectangle(position.x, position.y, characterSize.x, characterSize.y);
        // find the destructible wall we are about to collide with
        return gameState.getDestructibleWalls().stream()
                .filter(wall -> wall.getHitbox().overlaps(potentialHitbox))
                .findFirst();
    }

    /**
     * loop through all the explosion areas and explosion areas that are about to happen
     * collect them to a list and return the explosion area if it overlaps with given position
     * @param position to find the explosion with
     * @return explosion area if present
     */
    private Optional<Rectangle> findDangerHitbox(Vector2 position) {
        //use the position to make a character sized rectangle
        Vector2 characterSize = getCharacterSize();
        Rectangle potentialHitbox = new Rectangle(position.x, position.y, characterSize.x, characterSize.y);
        // collect all the future explosion and current explosion areas
        Set<Rectangle> futureExplosionAreas = new HashSet<>();
        gameState.getBombs().forEach(bomb -> {
            futureExplosionAreas.add(bomb.bombExplosionAreaHorizontal());
            futureExplosionAreas.add(bomb.bombExplosionAreaVertical());
        });
        gameState.getExplosions().forEach(explosion -> {
            futureExplosionAreas.add(explosion.getHitboxHorizontal().getHitbox());
            futureExplosionAreas.add(explosion.getHitboxVertical().getHitbox());
        });

        return futureExplosionAreas.stream()
                .filter(potentialHitbox::overlaps)
                .findFirst();
    }
    /**
     * get current position and add the movement direction to it
     * @return potential next position of the AI when it would keep moving in the current direction
     */
    private Vector2 getPotentialPosition() {
        // number of frames we should move our position ahead
        int framesToBuffer = 8;
        Vector2 position = getPosition();
        switch (currentDirection) {
            case LEFT:
                position.x = position.x - (movementSpeed * Gdx.graphics.getDeltaTime()) * framesToBuffer;
            case RIGHT:
                position.x = position.x + (movementSpeed * Gdx.graphics.getDeltaTime()) * framesToBuffer;
            case DOWN:
                position.y = position.y - (movementSpeed * Gdx.graphics.getDeltaTime()) * framesToBuffer;
            case UP:
                position.y = position.y + (movementSpeed * Gdx.graphics.getDeltaTime()) * framesToBuffer;
        }
        return position;
    }

    /**
     * switches the AI direction to opposite of previous direction
     * if AI position stays same for two frames, means that it has run into a wall and needs to get another direction
     * gets a new direction using getRandomDirectionExcept method
     */
    private void setDirectionWhenRunningAway() {
        if (previousPosition.equals(getPosition())) { // means that AI is stuck, try to change direction
            switch (currentDirection) {
                case UP:
                case DOWN:
                    currentDirection = getRandomDirectionExcept(MoveDirections.UP, MoveDirections.DOWN);
                    break;
                case LEFT:
                case RIGHT:
                    currentDirection = getRandomDirectionExcept(MoveDirections.LEFT, MoveDirections.RIGHT);
                    break;
            }
        }
        if (!currentState.equals(previousState)) {
            switch (currentDirection) {
                case UP:
                    currentDirection = MoveDirections.DOWN;
                    break;
                case DOWN:
                    currentDirection = MoveDirections.UP;
                    break;
                case LEFT:
                    currentDirection = MoveDirections.RIGHT;
                    break;
                case RIGHT:
                    currentDirection = MoveDirections.LEFT;
                    break;
            }
        }
    }

    /**
     * Get a random direction excluding the one specified in the parameters.
     * The point is to change the movement direction of the AI
     * @param direction that the AI is currently using
     * @return a new direction
     */
    private MoveDirections getRandomDirectionExcept(MoveDirections direction) {
        List<MoveDirections> directions = new ArrayList<>(Arrays.asList(MoveDirections.values()));
        directions.remove(direction);
        Random random = new Random();
        int index = random.nextInt(directions.size());
        return directions.get(index);
    }

    /**
     * Get a random direction excluding the one specified in the parameters.
     * The point is to change the movement direction of the AI
     * @param direction to avoid
     * @param direction1 to avoid
     * @return random direction
     */
    private MoveDirections getRandomDirectionExcept(MoveDirections direction, MoveDirections direction1) {
        List<MoveDirections> directions = new ArrayList<>(Arrays.asList(MoveDirections.values()));
        directions.remove(direction);
        directions.remove(direction1);
        Random random = new Random();
        int index = random.nextInt(directions.size());
        return directions.get(index);
    }
}

