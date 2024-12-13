package ee.taltech.iti0301.game.sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundHandler {
    /**
     * sound handler class holds all the different music files of the bomberman game
     * can start and stop any music
     */
    private final Music backgroundMusic1;
    private final Music backgroundMusic2;
    private final Music titleMusic;
    private final Sound bombDropSound;
    private final Sound explosionSound;
    private final Sound characterDeath;
    private final Sound victorySound;

    public SoundHandler() {
        backgroundMusic1 = Gdx.audio.newMusic(Gdx.files.internal("sounds\\Bomberman-Area1.ogg"));
        backgroundMusic1.setLooping(true);
        backgroundMusic2 = Gdx.audio.newMusic(Gdx.files.internal("sounds\\Bomberman-Area2.ogg"));
        backgroundMusic2.setLooping(true);
        titleMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds\\SuperBomberman-Title.ogg"));
        titleMusic.setLooping(true);
        bombDropSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\PlaceBomb.ogg"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds\\Explosion.ogg"));
        characterDeath = Gdx.audio.newSound(Gdx.files.internal("sounds\\Die.ogg"));
        victorySound = Gdx.audio.newSound(Gdx.files.internal("sounds\\Victory.ogg"));
    }
    public void playTitleMusic() {
        titleMusic.play();
    }
    public void stopTitleMusic() {
        titleMusic.stop();
    }
    public void playBomberManArea1() {
        backgroundMusic1.play();
    }
    public void stopBomberManArea1() {
        backgroundMusic1.stop();
    }
    public void playBomberManArea2() {
        backgroundMusic2.play();
    }
    public void stopBomberManArea2() {
        backgroundMusic2.stop();
    }
    public void bombDropSound() {
        bombDropSound.play();
    }
    public void explosionSound() {
        explosionSound.play();
    }
    public void characterDeath() {
        characterDeath.play();
    }
    public void victorySound() {
        victorySound.play();
    }
}
