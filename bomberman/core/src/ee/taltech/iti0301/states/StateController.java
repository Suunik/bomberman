package ee.taltech.iti0301.states;

import java.util.HashMap;
import java.util.Map;

import ee.taltech.iti0301.game.BombermanGdxGame;


public class StateController {
    private Map<Integer, State> gameStates;
    private BombermanGdxGame game;
    private State currentState;

    public StateController(BombermanGdxGame game) {
        gameStates = new HashMap<>();
        this.game = game;
        setState(State.States.CONNECTSTATE);
    }

    public void setState(State.States state) {
        switch (state) {
            case PLAYSTATE:
                currentState = new PlayState(this);
                break;
            case MENUSTATE:
                currentState = new LobbyState(this);
                break;
            case CONNECTSTATE:
                currentState = new ConnectState(this);
                break;
        }
        gameStates.put(state.ordinal(), currentState);
    }

    public void render() {
        currentState.render();
    }

    public void update(float deltaTime) {
        currentState.update(deltaTime);
    }

    public void dispose() {
        for (Map.Entry<Integer, State> entry : gameStates.entrySet()) {
            entry.getValue().dispose();
        }
    }

    public BombermanGdxGame getGame() {
        return game;
    }
}
