package ee.taltech.iti0301.states;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import ee.taltech.iti0301.game.BombermanGdxGame;
import ee.taltech.iti0301.game.bomb.Bomb;
import ee.taltech.iti0301.game.bomb.Explosion;
import ee.taltech.iti0301.game.character.*;
import ee.taltech.iti0301.game.character.Character;
import ee.taltech.iti0301.game.gameState.GameState;
import ee.taltech.iti0301.game.hitbox.Hitbox;
import ee.taltech.iti0301.game.map.World;
import ee.taltech.iti0301.game.powerups.PowerUp;
import ee.taltech.iti0301.game.sounds.SoundHandler;
import ee.taltech.iti0301.game.walls.DestructibleWall;
import ee.taltech.iti0301.game.walls.Wall;
import ee.taltech.iti0301.network.events.CurrentState;
import ee.taltech.iti0301.network.handlers.BombHandler;
import ee.taltech.iti0301.network.handlers.EnemyHandler;
import ee.taltech.iti0301.network.handlers.LobbyHandler;
import ee.taltech.iti0301.textures.BombermanTexture;
import ee.taltech.iti0301.network.handlers.PlayerHandler;


import javax.swing.*;
import java.util.*;

public class PlayState extends State {
    private GameState gameState = GameState.INSTANCE;
    private Player player;
    private Ai aiCharacter;
    private List<DeadCharacter> deadCharacters = new ArrayList<>();
    private SoundHandler sounds;
    private int numberOfBombsLastFrame = 0;
    private CharacterNameHandler nameHandler;

    public PlayState(StateController sc) {
        super(sc);
        createPlayer();
        createEnemy();
        nameHandler = new CharacterNameHandler();
        sounds = new SoundHandler();
        sounds.playBomberManArea1();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0.54f, 0.023f, 1);

        batch.begin();

        gameState.update(PlayerHandler.INSTANCE.getPlayers(), EnemyHandler.INSTANCE.getEnemies());

        for (Wall wall : gameState.getIndestructibleWalls()) {
            wall.draw(batch);
        }
        for (Wall wall : gameState.getDestructibleWalls()) {
            wall.draw(batch);
        }
        // draw all the power ups on the map
        for (PowerUp powerUp : gameState.getPowerUps()) {
            powerUp.drawSprite(batch);
        }

        // draw all the bomberman and get new hit boxes
        for (Character character : gameState.getCharacters()) {
            character.update(batch, gameState);
            nameHandler.drawCharacterName(batch, character);
        }

        // draw AI and get new hit-boxes
        for (Ai ai : gameState.getEnemies()) {
            ai.update(batch, gameState);
            nameHandler.drawCharacterName(batch, aiCharacter);
        }

        for (Explosion explosion : gameState.getExplosions()) {
            explosion.draw(batch);
        }

        deadCharacterHandler();
        explosionHandler();
        bombHandler();

        batch.end();
        // for drawing hit boxes
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.end();
        if (player != null)
        {
            // move only one character
            player.inputMovement();

            // check if character pressed bomb input, which should return a new Bomb object
            player.inputDropBomb();
        }
    }

    @Override
    public void update(float deltaTime) {
        if (player == null) {
            return;
        }
        PlayerHandler.INSTANCE.movePlayer(player);

    }
    private void checkStatus() {
        if (checkWinConditions()) {
            if (showWinMessage() == 0) {
                gameState.clearAllLists();
                World.INSTANCE.requestNewMap();
                sounds.stopBomberManArea1();
                this.player = null;
                sc.setState(States.MENUSTATE);
            } else if (showWinMessage() == 1) {
                CurrentState currentState = new CurrentState();
                currentState.gameStatus = false;
                BombermanGdxGame.client.sendUDP(currentState);
                BombermanGdxGame.client.close();
            }
        } else if (checkTieConditions()) {
            if (showWTieMessage() == 0) {
                gameState.clearAllLists();
                World.INSTANCE.requestNewMap();
                sounds.stopBomberManArea1();
                this.player = null;
                sc.setState(States.MENUSTATE);
            } else if (showWTieMessage() == 1) {
                CurrentState currentState = new CurrentState();
                currentState.gameStatus = false;
                BombermanGdxGame.client.sendUDP(currentState);
                BombermanGdxGame.client.close();
            }
        } else if (checkEnemyWin()) {
            if (showEnemyWinMessage() == 0) {
                gameState.clearAllLists();
                World.INSTANCE.requestNewMap();
                sounds.stopBomberManArea1();
                this.player = null;
                sc.setState(States.MENUSTATE);
            } else if (showEnemyWinMessage() == 1) {
                CurrentState currentState = new CurrentState();
                currentState.gameStatus = false;
                BombermanGdxGame.client.sendUDP(currentState);
                BombermanGdxGame.client.close();
            }
        }
    }

    private boolean checkWinConditions() {
        return PlayerHandler.INSTANCE.getPlayers().size() == 1 && EnemyHandler.INSTANCE.getEnemies().isEmpty();
    }

    private boolean checkTieConditions() {
        return PlayerHandler.INSTANCE.getPlayers().isEmpty() && EnemyHandler.INSTANCE.getEnemies().isEmpty();
    }

    private boolean checkEnemyWin() {
        return PlayerHandler.INSTANCE.getPlayers().isEmpty() && EnemyHandler.INSTANCE.getEnemies().size() > 0;
    }

    private int showWinMessage() {
        String[] options = {"Back to game lobby", "Quit game"};
        int option = JOptionPane.showOptionDialog(null, "Player " +
                gameState.getCharacters().get(0).getUsername() + " has won!", "Message",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        return option;
    }

    private int showWTieMessage() {
        String[] options = {"Back to game lobby", "Quit game"};
        return JOptionPane.showOptionDialog(null, "The game ended in a tie!", "Message",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
    }

    private int showEnemyWinMessage() {
        String[] options = {"Back to game lobby", "Quit game"};
        int option = JOptionPane.showOptionDialog(null, "Enemy was victorious!", "Message",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        return option;
    }

    private void createPlayer() {
        player = new Player(PlayerHandler.INSTANCE.getUsername());
        player.setCharacterPosition(PlayerHandler.INSTANCE.getPosition().x, PlayerHandler.INSTANCE.getPosition().y);
        PlayerHandler.INSTANCE.newPlayer(player);
    }

    private void createEnemy() {
        if (LobbyHandler.INSTANCE.getUsers().size() != 4) {
            aiCharacter = EnemyHandler.INSTANCE.createNewEnemy();
            aiCharacter.setCharacterPosition(990, 100);
        }
    }

    private void bombHandler() {
        Set<Bomb> bombsToRemove = new HashSet<>();
        Set<Hitbox> hitboxesToRemove = new HashSet<>();
        Set<Bomb> bombs = new HashSet<>(gameState.getBombs());
        if (bombs.size() > numberOfBombsLastFrame) {
            sounds.bombDropSound();
        }
        numberOfBombsLastFrame = bombs.size();
        // update bombs
        for (Bomb bomb : bombs) {
            bomb.update(batch);
            if (bomb.shouldExplode()) {
                hitboxesToRemove.add(bomb.getHitBox());
                bombsToRemove.add(bomb);

                gameState.addExplosion(bomb.getExplosion());
                sounds.explosionSound();
            }
        }
        gameState.removeAllHitboxes(hitboxesToRemove);
        BombHandler.INSTANCE.removeBomb(bombsToRemove);
    }
    private void explosionHandler() {
        Set<Explosion> explosionsToRemove = new HashSet<>();
        Set<DestructibleWall> wallsCollided = new HashSet<>();
        Set<Player> playersCollided = new HashSet<>();
        Set<Ai> enemiesCollided = new HashSet<>();
        Set<Bomb> bombsCollided = new HashSet<>();

        for (Explosion explosion : gameState.getExplosions()) {
            explosion.update(batch);
            wallsCollided.addAll(explosion.checkForWallCollision(gameState.getDestructibleWalls()));
            playersCollided.addAll(explosion.checkForCharacterCollision(gameState.getCharacters()));
            enemiesCollided.addAll(explosion.checkForAiCollision(gameState.getEnemies()));
            bombsCollided.addAll(explosion.checkForBombCollision(gameState.getBombs()));
            if(explosion.explosionEnded()) {
                explosionsToRemove.add(explosion);
            }
        }

        // remove all the hitboxes from collidable hitbox list

        wallsCollided.forEach(wall -> {
            gameState.removeHitbox(wall.getHitbox());
            wall.getPowerUp().ifPresent(powerUp -> gameState.addPowerUp(powerUp));
        });
        // remove all the walls collided from the list
        gameState.removeAllDestructibleWalls(wallsCollided);
        // remove all characters collided from the list
        PlayerHandler.INSTANCE.removeCollidedPlayers(playersCollided);
        if (playersCollided.contains(player)) {
            player = null;
        }
        playersCollided.forEach(player -> {
            deadCharacters.add(new DeadCharacter(player.getPosition(),
                    player.getCharacterWidth(), player.getCharacterHeight()));
            sounds.characterDeath();
                });
        // remove all AIs collided from the list
        EnemyHandler.INSTANCE.removeCollidedEnemies(enemiesCollided);
        enemiesCollided.forEach(enemy -> {
                    deadCharacters.add(new DeadCharacter(enemy.getPosition(),
                            enemy.getCharacterHeight(), enemy.getCharacterWidth()));
                    sounds.characterDeath();
                });
        // end explosion
        gameState.removeAllExplosions(explosionsToRemove);
    }
    private void deadCharacterHandler() {
        List<DeadCharacter> toRemove = new ArrayList<>();
        for (DeadCharacter character : deadCharacters) {
            if (character.animationIsDone()) {
                checkStatus();
                toRemove.add(character);
                continue;
            }
            character.update(batch);
        }
        deadCharacters.removeAll(toRemove);
    }
    @Override
    public void dispose() {
        BombermanTexture.dispose();
        nameHandler.dispose();
    }
}
