package ee.taltech.iti0301.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import ee.taltech.iti0301.network.client.GameClient;
import ee.taltech.iti0301.states.State;
import ee.taltech.iti0301.states.StateController;

public class BombermanGdxGame extends ApplicationAdapter {
	private StateController sc;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	public static GameClient client;

	@Override
	public void create () {
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		client = new GameClient();
		sc = new StateController(this);
	}

	@Override
	public void render () {
		sc.render();
		sc.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose () {
		sc.dispose();
		batch.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}
	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	public static BombermanGdxGame getInstance() {
		return (BombermanGdxGame) Gdx.app.getApplicationListener();
	}

	public void changeState() {
		sc.setState(State.States.PLAYSTATE);
	}
}
