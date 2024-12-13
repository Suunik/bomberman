package ee.taltech.iti0301.game.bomb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import ee.taltech.iti0301.game.animations.ExplosionAnimation;
import ee.taltech.iti0301.game.character.Ai;
import ee.taltech.iti0301.game.character.Player;
import ee.taltech.iti0301.game.gameState.GameState;
import ee.taltech.iti0301.game.hitbox.Hitbox;
import ee.taltech.iti0301.game.walls.DestructibleWall;
import ee.taltech.iti0301.game.walls.IndestructibleWall;
import ee.taltech.iti0301.game.walls.Wall;
import ee.taltech.iti0301.textures.BombermanTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Explosion {
    private final float explosionSpriteSize = 1.2f;
    private final ExplosionAnimation animation;
    private Sprite explosionCentreSprite;
    private Sprite explosionMiddleSprite;
    private Sprite explosionEndSprite;
    private List<Sprite> explosionElementsLeft = new ArrayList<>();
    private List<Sprite> explosionElementsUp = new ArrayList<>();
    private List<Sprite> explosionElementsRight = new ArrayList<>();
    private List<Sprite> explosionElementsDown = new ArrayList<>();
    private Hitbox hitboxHorizontal;
    private Hitbox hitboxVertical;
    private final float explosionWidth = 60 * explosionSpriteSize;
    private final float explosionHeight = 60 * explosionSpriteSize;
    private float explosionTimer = 0.3f;
    private boolean explosionEnd = false;

    /**
     * sets the explosions area, calculates how long the explosion can go for each side
     * @param explosionSize of the explosion
     * @param position of the explosion
     */
    public Explosion(int explosionSize, Vector2 position) {
        setExplosionSprites(position);
        setSpriteSizes();
        createExplosionElements(explosionSize);
        animation = new ExplosionAnimation(position, explosionHeight, explosionWidth, explosionTimer);
    }

    /**
     * updates the explosion sprites each frame using explosion animation and draws the explosion to the batch given
     * @param batch to draw the explosion onto
     */
    public void update(Batch batch) {
        updateSprites();
        draw(batch);
        explosionTimer();
    }

    /**
     * draw each element of the explosion
     * @param batch to draw the explosion onto
     */
    public void draw(Batch batch) {
        explosionCentreSprite.draw(batch);
        for (Sprite sprite : explosionElementsLeft) {
            sprite.draw(batch);
        }
        for (Sprite sprite : explosionElementsUp) {
            sprite.draw(batch);
        }
        for (Sprite sprite : explosionElementsRight) {
            sprite.draw(batch);
        }
        for (Sprite sprite : explosionElementsDown) {
            sprite.draw(batch);
        }
    }

    /**
     * updates explosions sprites using explosion animation
     * also updates explosion animation timer
     */
    private void updateSprites() {
        explosionCentreSprite = animation.getFrameCentre();

        explosionEndSprite = animation.getFrameEnd();
        explosionMiddleSprite = animation.getFrameMiddle();

        updateLeftSprites();
        updateUpSprites();
        updateRightSprites();
        updateDownSprites();

        animation.updateAnimationTimer(Gdx.graphics.getDeltaTime());
    }

    /**
     * updates sprites on the left side of the explosion
     */
    private void updateLeftSprites() {
        List<Sprite> newList = new ArrayList<>();
        for (int index = 0; index < explosionElementsLeft.size(); ++index) {
            Vector2 previousSpritePos =
                    new Vector2(explosionElementsLeft.get(index).getX(), explosionElementsLeft.get(index).getY());
            Sprite sprite;
            if (index == explosionElementsLeft.size() - 1) {
                sprite = new Sprite(explosionEndSprite);
            } else {
                sprite = new Sprite(explosionMiddleSprite);
            }
            sprite.setPosition(previousSpritePos.x, previousSpritePos.y);
            newList.add(sprite);
        }

        explosionElementsLeft = newList;
    }

    /**
     * updates sprites on the up of the explosion
     */
    private void updateUpSprites() {
        List<Sprite> newList = new ArrayList<>();
        for (int index = 0; index < explosionElementsUp.size(); ++index) {
            Vector2 previousSpritePos =
                    new Vector2(explosionElementsUp.get(index).getX(), explosionElementsUp.get(index).getY());
            Sprite sprite;
            if (index == explosionElementsUp.size() - 1) {
                sprite = new Sprite(explosionEndSprite);
            } else {
                sprite = new Sprite(explosionMiddleSprite);
            }
            sprite.setPosition(previousSpritePos.x, previousSpritePos.y);
            sprite.rotate90(true);
            newList.add(sprite);
        }

        explosionElementsUp = newList;
    }

    /**
     * updates the sprites on the right side of the explosion
     */
    private void updateRightSprites() {
        List<Sprite> newList = new ArrayList<>();
        for (int index = 0; index < explosionElementsRight.size(); ++index) {
            Vector2 previousSpritePos =
                    new Vector2(explosionElementsRight.get(index).getX(), explosionElementsRight.get(index).getY());
            Sprite sprite;
            if (index == explosionElementsRight.size() - 1) {
                sprite = new Sprite(explosionEndSprite);
            } else {
                sprite = new Sprite(explosionMiddleSprite);
            }
            sprite.setPosition(previousSpritePos.x, previousSpritePos.y);
            sprite.flip(true, false);
            newList.add(sprite);
        }

        explosionElementsRight = newList;
    }

    /**
     * updates the sprites on bottom of the explosion
     */
    private void updateDownSprites() {
        List<Sprite> newList = new ArrayList<>();
        for (int index = 0; index < explosionElementsDown.size(); ++index) {
            Vector2 previousSpritePos =
                    new Vector2(explosionElementsDown.get(index).getX(), explosionElementsDown.get(index).getY());
            Sprite sprite;
            if (index == explosionElementsDown.size() - 1) {
                sprite = new Sprite(explosionEndSprite);
            } else {
                sprite = new Sprite(explosionMiddleSprite);
            }
            sprite.setPosition(previousSpritePos.x, previousSpritePos.y);
            sprite.rotate90(false);
            newList.add(sprite);
        }

        explosionElementsDown = newList;
    }

    /**
     * sets the initial sprites of the explosion
     * @param position to set the middle sprite to
     */
    private void setExplosionSprites(Vector2 position) {
        Texture explosionTexture = BombermanTexture.getExplosionTexture();
        explosionCentreSprite = new Sprite(explosionTexture, 46, 47, 15, 16);
        // assign explosion centre location here (everything else will get positions from it)
        explosionCentreSprite.setPosition(position.x, position.y);
        // assign sprites for middle part and end part of the explosions
        explosionMiddleSprite = new Sprite(explosionTexture, 29, 47, 15 ,16);
        explosionEndSprite = new Sprite(explosionTexture, 13, 47, 15 ,16);
    }
    public Hitbox getHitboxHorizontal() {
        return hitboxHorizontal;
    }
    public Hitbox getHitboxVertical() {
        return hitboxVertical;
    }
    private void explosionTimer() {
        float dt = Gdx.graphics.getDeltaTime();
        explosionTimer -= dt;

        if (explosionTimer <= 0) {
            explosionEnd = true;
        }
    }
    /**
     * Collect all the walls that was collided with to a set
     * @param destructibleWalls all the worlds walls that needs checking
     * @return set of walls
     */
    public Set<DestructibleWall> checkForWallCollision(List<DestructibleWall> destructibleWalls) {
        return destructibleWalls.stream()
                .filter(wall -> wall.getHitbox().overlaps(hitboxHorizontal) ||
                        wall.getHitbox().overlaps(hitboxVertical))
                .collect(Collectors.toSet());
    }
    /**
     * Collect every character that was hit by the explosion to a list
     * @param players to check
     * @return players hit
     */
    public Set<Player> checkForCharacterCollision(List<Player> players) {
        return players.stream()
                .filter(player ->
                        player.getHitbox().overlaps(hitboxHorizontal) ||
                                player.getHitbox().overlaps(hitboxVertical))
                .collect(Collectors.toSet());
    }
    /**
     * Collect every character that was hit by the explosion to a list
     * @param ais to check
     * @return players hit
     */
    public Set<Ai> checkForAiCollision(List<Ai> ais) {
        return ais.stream()
                .filter(ai ->
                        ai.getHitbox().overlaps(hitboxHorizontal) ||
                                ai.getHitbox().overlaps(hitboxVertical))
                .collect(Collectors.toSet());
    }

    /**
     * returns a list of bombs that was collided with the explosion
     * @param bombs set to find the collided bombs from
     * @return bombs collided with
     */
    public Set<Bomb> checkForBombCollision(Set<Bomb> bombs) {
        return bombs.stream()
                .filter(bomb ->
                        bomb.getHitBox().overlaps(hitboxHorizontal) || bomb.getHitBox().overlaps(hitboxVertical))
                .collect(Collectors.toSet());
    }

    /**
     *
     * @return true if explosions timer has run out
     */
    public boolean explosionEnded() {
        return explosionEnd;
    }

    /**
     * calculates the explosion sizes to each side and adds each element to corresponding list
     * @param explosionSize max size of the explosion, explosions size will be this by default
     */
    private void createExplosionElements(int explosionSize) {
        ExplosionSizeAlgorithm algorithm = new ExplosionSizeAlgorithm();
        algorithm.calculateClosestWalls();

        // Left and Down should always be negative values, get the absolute values instead.
        int explosionSizeLeft = Math.min(Math.abs(algorithm.getExplosionSizeLeft()), explosionSize);
        int explosionSizeUp = Math.min(algorithm.getExplosionSizeUp(), explosionSize);
        int explosionSizeRight = Math.min(algorithm.getExplosionSizeRight(), explosionSize);
        int explosionSizeDown = Math.min(Math.abs(algorithm.getExplosionSizeDown()), explosionSize);
        // set the middle parts of the explosions
        setExplosionMiddleElementsAndAddToList(explosionSizeLeft, "left");
        setExplosionMiddleElementsAndAddToList(explosionSizeUp, "up");
        setExplosionMiddleElementsAndAddToList(explosionSizeRight, "right");
        setExplosionMiddleElementsAndAddToList(explosionSizeDown, "down");
        // set the end parts of the explosions
        setExplosionEnds(explosionSizeLeft, explosionSizeUp, explosionSizeRight, explosionSizeDown);

        setExplosionHitbox(explosionSizeLeft, explosionSizeUp, explosionSizeRight, explosionSizeDown);
    }

    /**
     * adds "explosionSize" amount of elements to the list specified by direction
     * @param explosionSize number of elements to add to the list
     * @param direction - which list to add the explosions into
     */
    private void setExplosionMiddleElementsAndAddToList(int explosionSize, String direction) {
        while (explosionSize > 1) {
            Sprite nextSprite = new Sprite(explosionMiddleSprite);

            switch (direction) {
                case "left": {
                    explosionElementsLeft.add(setSpriteLeftPos(nextSprite));
                    break;
                }
                case "up": {
                    nextSprite.rotate90(true);
                    explosionElementsUp.add(setSpriteUpPos(nextSprite));
                    break;
                }
                case "right": {
                    nextSprite.flip(true, false);
                    explosionElementsRight.add(setSpriteRightPos(nextSprite));
                    break;
                }
                case "down": {
                    nextSprite.rotate90(false);
                    explosionElementsDown.add(setSpriteDownPos(nextSprite));
                    break;
                }
            }
            --explosionSize;
        }
    }

    /**
     * sets the explosion end elements and adds them to the lists
     * @param explosionSizeLeft size of the explosion to the left
     * @param explosionSizeUp size of the explosion to up
     * @param explosionSizeRight size of the explosion to right
     * @param explosionSizeDown size of the explosion to down
     */
    private void setExplosionEnds(int explosionSizeLeft, int explosionSizeUp,
                                  int explosionSizeRight, int explosionSizeDown) {
        if (explosionSizeLeft > 0) {
            explosionElementsLeft.add(setEndLeft());
        }
        if (explosionSizeUp > 0) {
            explosionElementsUp.add(setEndUp());
        }
        if (explosionSizeRight > 0) {
            explosionElementsRight.add(setEndRight());
        }
        if (explosionSizeDown > 0) {
            explosionElementsDown.add(setEndDown());
        }
    }

    /**
     * sets the end element to the left depending on the last element to the left
     * if there are no elements on the left, adds the end element depending on the position of the middle element
     * @return end element sprite with correct position
     */
    private Sprite setEndLeft() {
        Vector2 lastElementLeftPos;

        if (explosionElementsLeft.size() > 0) {
            Sprite lastOnTheLeft = explosionElementsLeft.get(explosionElementsLeft.size() - 1);
            lastElementLeftPos = new Vector2(lastOnTheLeft.getX(), lastOnTheLeft.getY());
        } else {
            lastElementLeftPos = new Vector2(explosionCentreSprite.getX(), explosionCentreSprite.getY());
        }
        Sprite leftEnd = new Sprite(explosionEndSprite);
        leftEnd.setPosition(lastElementLeftPos.x - explosionWidth, lastElementLeftPos.y);

        return leftEnd;
    }
    /**
     * sets the end element to the up depending on the last element to the up
     * if there are no elements on the up, adds the end element depending on the position of the middle element
     * @return end element sprite with correct position
     */
    private Sprite setEndUp() {
        Vector2 lastElementUpPos;

        if (explosionElementsUp.size() > 0) {
            Sprite lastOnTheUp = explosionElementsUp.get(explosionElementsUp.size() - 1);
            lastElementUpPos = new Vector2(lastOnTheUp.getX(), lastOnTheUp.getY());
        } else {
            lastElementUpPos = new Vector2(explosionCentreSprite.getX(), explosionCentreSprite.getY());
        }
        Sprite upEnd = new Sprite(explosionEndSprite);
        upEnd.setPosition(lastElementUpPos.x, lastElementUpPos.y + explosionWidth);
        upEnd.rotate90(true);

        return upEnd;

    }
    /**
     * sets the end element to the right depending on the last element to the right
     * if there are no elements on the right, adds the end element depending on the position of the middle element
     * @return end element sprite with correct position
     */
    private Sprite setEndRight() {
        Vector2 lastElementRightPos;

        if (explosionElementsRight.size() > 0) {
            Sprite lastOnTheRight = explosionElementsRight.get(explosionElementsRight.size() - 1);
            lastElementRightPos = new Vector2(lastOnTheRight.getX(), lastOnTheRight.getY());
        } else {
            lastElementRightPos = new Vector2(explosionCentreSprite.getX(), explosionCentreSprite.getY());
        }

        Sprite rightEnd = new Sprite(explosionEndSprite);
        rightEnd.setPosition(lastElementRightPos.x + explosionWidth, lastElementRightPos.y);
        rightEnd.flip(true, false);

        return rightEnd;
    }
    /**
     * sets the end element to the down depending on the last element to the down
     * if there are no elements on the down, adds the end element depending on the position of the middle element
     * @return end element sprite with correct position
     */
    private Sprite setEndDown() {
        Vector2 lastElementDownPos;

        if (explosionElementsDown.size() > 0) {
            Sprite lastOnTheDown = explosionElementsDown.get(explosionElementsDown.size() - 1);
            lastElementDownPos = new Vector2(lastOnTheDown.getX(), lastOnTheDown.getY());
        } else {
            lastElementDownPos = new Vector2(explosionCentreSprite.getX(), explosionCentreSprite.getY());
        }

        Sprite downEnd = new Sprite(explosionEndSprite);
        downEnd.setPosition(lastElementDownPos.x, lastElementDownPos.y - explosionWidth);
        downEnd.rotate90(false);

        return downEnd;
    }

    /**
     * sets the left sprite element position depending on the last element to the left
     * @param sprite to set
     * @return sprite with the correct position
     */
    private Sprite setSpriteLeftPos(Sprite sprite) {
        if (explosionElementsLeft.size() == 0) {
            // get last position from the middle element if there are no left elements
            sprite.setPosition(explosionCentreSprite.getX() - explosionWidth, explosionCentreSprite.getY());
        } else {
            Sprite lastElement = explosionElementsLeft.get(explosionElementsLeft.size() - 1);
            sprite.setPosition(lastElement.getX() - explosionWidth, lastElement.getY());
        }
        return sprite;
    }
    /**
     * sets the left sprite element position depending on the last element to the up
     * @param sprite to set
     * @return sprite with the correct position
     */
    private Sprite setSpriteUpPos(Sprite sprite) {
        if (explosionElementsUp.size() == 0) {
            // get last position from the middle element if there are no left elements
            sprite.setPosition(explosionCentreSprite.getX(), explosionCentreSprite.getY() + explosionWidth);
        } else {
            Sprite lastElement = explosionElementsUp.get(explosionElementsUp.size() - 1);
            sprite.setPosition(lastElement.getX(), lastElement.getY() + explosionWidth);
        }
        return sprite;
    }
    /**
     * sets the left sprite element position depending on the last element to the right
     * @param sprite to set
     * @return sprite with the correct position
     */
    private Sprite setSpriteRightPos(Sprite sprite) {
        if (explosionElementsRight.size() == 0) {
            // get last position from the middle element if there are no left elements
            sprite.setPosition(explosionCentreSprite.getX() + explosionWidth, explosionCentreSprite.getY());
        } else {
            Sprite lastElement = explosionElementsRight.get(explosionElementsRight.size() - 1);
            sprite.setPosition(lastElement.getX() + explosionWidth, lastElement.getY());
        }
        return sprite;
    }
    /**
     * sets the left sprite element position depending on the last element to the down
     * @param sprite to set
     * @return sprite with the correct position
     */
    private Sprite setSpriteDownPos(Sprite sprite) {
        if (explosionElementsDown.size() == 0) {
            // get last position from the middle element if there are no left elements
            sprite.setPosition(explosionCentreSprite.getX(), explosionCentreSprite.getY() - explosionWidth);
        } else {
            Sprite lastElement = explosionElementsDown.get(explosionElementsDown.size() - 1);
            sprite.setPosition(lastElement.getX(), lastElement.getY() - explosionWidth);
        }
        return sprite;
    }

    /**
     * updates sprite sizes using explosions width and height
     */
    private void setSpriteSizes() {
        explosionCentreSprite.setSize(explosionWidth, explosionHeight);
        explosionMiddleSprite.setSize(explosionWidth, explosionHeight);
        explosionEndSprite.setSize(explosionWidth, explosionHeight);
    }

    /**
     * sets the explosions hitbox using explosion sizes to each side
     * @param explosionSizeLeft hitbox size to the left
     * @param explosionSizeUp hitbox size to up
     * @param explosionSizeRight hitbox size to right
     * @param explosionSizeDown hitbox size to down
     */
    private void setExplosionHitbox(int explosionSizeLeft, int explosionSizeUp,
                                    int explosionSizeRight, int explosionSizeDown) {
        // +1 is for the centre part
        int hitboxSizeHorizontal = explosionSizeLeft + explosionSizeRight + 1;
        int hitboxSizeVertical = explosionSizeUp + explosionSizeDown + 1;

        Vector2 explosionStartPosHorizontal;
        Vector2 explosionStartPosVertical;

        if (explosionElementsLeft.size() >= 1) {
            Sprite lastElementToTheLeft = explosionElementsLeft.get(explosionElementsLeft.size() - 1);
            explosionStartPosHorizontal = new Vector2(lastElementToTheLeft.getX(), lastElementToTheLeft.getY());
        } else {
            explosionStartPosHorizontal = new Vector2(explosionCentreSprite.getX(), explosionCentreSprite.getY());
        }

        if (explosionElementsDown.size() >= 1) {
            Sprite lastElementToTheDown = explosionElementsDown.get(explosionElementsDown.size() - 1);
            explosionStartPosVertical = new Vector2(lastElementToTheDown.getX(), lastElementToTheDown.getY());
        } else {
            explosionStartPosVertical = new Vector2(explosionCentreSprite.getX(), explosionCentreSprite.getY());
        }
        hitboxHorizontal = new Hitbox(explosionStartPosHorizontal,
                explosionHeight, explosionWidth * hitboxSizeHorizontal);
        hitboxVertical = new Hitbox(explosionStartPosVertical,
                explosionHeight * hitboxSizeVertical, explosionWidth);
    }
    private class ExplosionSizeAlgorithm {
        /**
         * Nested class for calculating wall distances from the explosion centre
         */
        int explosionSizeLeft = -999;
        int explosionSizeUp = 999;
        int explosionSizeRight = 999;
        int explosionSizeDown = -999;

        /**
         * gets the distance of the wall on the Y axis from bombs center position
         * @param wallPos to check against bombs center
         * @param bombCentrePos position of the bombs center
         * @return distance of the wall from bombs centre
         */
        private Optional<Integer> getWallDistanceYAxis(Vector2 wallPos, Vector2 bombCentrePos) {
            float posXDiff = wallPos.x - bombCentrePos.x;

            if (posXDiff > 5 || posXDiff < -5) {
                return Optional.empty();
            }
            int wallDistance = Math.round((wallPos.y - bombCentrePos.y) / explosionHeight);
            return Optional.of(wallDistance);
        }
        /**
         * gets the distance of the wall on the X axis from bombs center position
         * @param wallPos to check against bombs center
         * @param bombCentrePos position of the bombs center
         * @return distance of the wall from bombs centre
         */
        private Optional<Integer> getWallDistanceXAxis(Vector2 wallPos, Vector2 bombCentrePos) {
            float posYDiff = wallPos.y - bombCentrePos.y;

            if (posYDiff > 5 || posYDiff < -5) {
                return Optional.empty();
            }
            int wallDistance = Math.round((wallPos.x - bombCentrePos.x) / explosionHeight);
            return Optional.of(wallDistance);
        }

        /**
         * finds the closest walls from the bombs centre
         */
        private void calculateClosestWalls() {
            // find the closest wall to each direction
            List<DestructibleWall> destructibleWalls = GameState.INSTANCE.getDestructibleWalls();
            List<IndestructibleWall> inDestructibleWalls = GameState.INSTANCE.getIndestructibleWalls();

            calculateWallDistances(destructibleWalls);
            calculateWallDistances(inDestructibleWalls);
        }

        /**
         * finds the closest walls from the explosions centre
         * saves the closest distance to each site as class variable
         * if wall is indestructible takes one distance off because indestructible walls will not let explosion
         * through
         * @param walls to set the distance from
         */
        private void calculateWallDistances(List<? extends Wall> walls) {
            Vector2 bombCentrePos = new Vector2(explosionCentreSprite.getX(), explosionCentreSprite.getY());

            for (Wall wall : walls) {
                Optional<Integer> currentDistanceY = getWallDistanceYAxis(wall.getPosition(), bombCentrePos);
                Optional<Integer> currentDistanceX = getWallDistanceXAxis(wall.getPosition(), bombCentrePos);
                // if the current distance on Y axis is larger than 0, means that the wall is located up
                // if less than 0 means that the wall is located down
                if (currentDistanceY.isPresent()) {
                    int currentDistance = currentDistanceY.get();
                    if (wall instanceof IndestructibleWall) {
                        currentDistance--;
                    }
                    if (currentDistance >= 0 && currentDistance < explosionSizeUp) {
                        explosionSizeUp = currentDistance;
                    }
                    if (wall instanceof IndestructibleWall) {
                        currentDistance = currentDistance + 2;
                    }
                    if (currentDistance <= 0 && currentDistance > explosionSizeDown) {
                        explosionSizeDown = currentDistance;
                    }
                }
                // if the current distance on X axis is larger than 0, means that the wall is located right
                // if less than 0 means that the wall is on the left
                if (currentDistanceX.isPresent()) {
                    int currentDistance = currentDistanceX.get();
                    if (wall instanceof IndestructibleWall) {
                        currentDistance++;
                    }
                    if (currentDistance <= 0 && currentDistance > explosionSizeLeft) {
                        explosionSizeLeft = currentDistance;
                    }
                    if (wall instanceof IndestructibleWall) {
                        currentDistance = currentDistance - 2;
                    }
                    if (currentDistance >= 0 && currentDistance < explosionSizeRight) {
                        explosionSizeRight = currentDistance;
                    }
                }
            }
        }

        public int getExplosionSizeLeft() {
            return explosionSizeLeft;
        }

        public int getExplosionSizeUp() {
            return explosionSizeUp;
        }

        public int getExplosionSizeRight() {
            return explosionSizeRight;
        }

        public int getExplosionSizeDown() {
            return explosionSizeDown;
        }
    }
}
