package ee.taltech.iti0301.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ee.taltech.iti0301.game.BombermanGdxGame;

public abstract class State {
    protected StateController sc;
    protected BombermanGdxGame game;
    protected SpriteBatch batch;
    protected ShapeRenderer shapeRenderer;

    protected State(StateController sc) {
        this.sc = sc;
        game = sc.getGame();
        batch = game.getBatch();
        shapeRenderer = game.getShapeRenderer();
    }
    public abstract void update(float deltaTime);
    public abstract void render();
    public abstract void dispose();

    public enum States {
        PLAYSTATE, MENUSTATE, CONNECTSTATE
    }
}
