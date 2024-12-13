package ee.taltech.iti0301.game.gameState;

import ee.taltech.iti0301.game.bomb.Bomb;
import ee.taltech.iti0301.game.bomb.Explosion;
import ee.taltech.iti0301.game.character.Ai;
import ee.taltech.iti0301.game.character.Character;
import ee.taltech.iti0301.game.character.Player;
import ee.taltech.iti0301.game.hitbox.Hitbox;
import ee.taltech.iti0301.game.powerups.PowerUp;
import ee.taltech.iti0301.game.walls.DestructibleWall;
import ee.taltech.iti0301.game.walls.IndestructibleWall;
import ee.taltech.iti0301.network.handlers.EnemyHandler;
import ee.taltech.iti0301.network.handlers.PlayerHandler;

import java.util.*;

public class GameState {
    /**
     * Game state class holds all the information about the games current state
     */
    public static final GameState INSTANCE = new GameState();
    private Set<PowerUp> powerUps = new HashSet<>();
    private List<Player> characters = new ArrayList<>();
    private List<Ai> enemies = new ArrayList<>();
    private Set<Bomb> bombs = new HashSet<>();
    private Set<Explosion> explosions = new HashSet<>();
    private List<IndestructibleWall> indestructibleWalls = new ArrayList<>();
    private List<DestructibleWall> destructibleWalls = new ArrayList<>();
    private Set<Hitbox> collidableHitBoxes = new HashSet<>();
    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
    }
    public void removeAllCharacters(Set<Player> charactersToRemove) {
        characters.removeAll(charactersToRemove);
    }
    public void removeCharacter(Character character) {
        characters.remove(character);
    }

    public void removeAllEnemies(Set<Ai> aisToRemove) {
        enemies.removeAll(aisToRemove);
    }
    public void emptyEnemyList() {
        enemies.clear();
    }

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
    }

    public void removeAllBombs(Set<Bomb> bombsToRemove) {
        bombs.removeAll(bombsToRemove);
    }
    public void addExplosion(Explosion explosion) {
        explosions.add(explosion);
    }
    public void removeAllExplosions(Set<Explosion> explosionsToRemove) {
        explosions.removeAll(explosionsToRemove);
    }
    public void setIndestructibleWalls(List<IndestructibleWall> walls) {
        indestructibleWalls = walls;
    }
    public void setDestructibleWalls(List<DestructibleWall> walls) {
        destructibleWalls = walls;
    }
    public void removeAllDestructibleWalls(Set<DestructibleWall> walls) {
        destructibleWalls.removeAll(walls);
    }
    public void removeHitbox(Hitbox hitbox) {
        collidableHitBoxes.remove(hitbox);
    }
    public void removeAllHitboxes(Set<Hitbox> hitboxesToRemove) {
        collidableHitBoxes.removeAll(hitboxesToRemove);
    }
    public Set<PowerUp> getPowerUps() {
        return powerUps;
    }

    public List<Player> getCharacters() {
        return characters;
    }

    public List<Ai> getEnemies() {
        return enemies;
    }
    public void update(LinkedList<Player> players, LinkedList<Ai> enemyList) {
        updateCharacters(players);
        updateEnemies(enemyList);
        updateCollidableHitboxes();
    }
    private void updateCollidableHitboxes() {
        Set<Hitbox> currentHitboxes = new HashSet<>();

        destructibleWalls.forEach(wall -> currentHitboxes.add(wall.getHitbox()));
        indestructibleWalls.forEach(wall -> currentHitboxes.add(wall.getHitbox()));
        bombs.forEach(bomb -> currentHitboxes.add(bomb.getHitBox()));

        collidableHitBoxes = currentHitboxes;
    }
    private void updateCharacters(LinkedList<Player> players) {
        for (Player player : players) {
            if (!characters.contains(player)) {
                characters.add(player);
            }
        }
    }

    private void updateEnemies(LinkedList<Ai> enemyList) {
        for (Ai ai : enemyList) {
            if (!enemies.contains(ai)) {
                enemies.add(ai);
            }
        }
    }

    public Set<Bomb> getBombs() {
        return bombs;
    }

    public Set<Explosion> getExplosions() {
        return explosions;
    }

    public List<IndestructibleWall> getIndestructibleWalls() {
        return indestructibleWalls;
    }

    public List<DestructibleWall> getDestructibleWalls() {
        return destructibleWalls;
    }

    public Set<Hitbox> getCollidableHitBoxes() {
        return collidableHitBoxes;
    }
    public void clearAllLists() {
        destructibleWalls.clear();
        indestructibleWalls.clear();
        PowerUp.resetIdCounter();
        powerUps.clear();
        collidableHitBoxes.clear();
        explosions.clear();
        bombs.clear();
        characters.clear();
        PlayerHandler.INSTANCE.emptyPlayersList();
        EnemyHandler.INSTANCE.removeAllEnemies();
    }

}
