package ee.taltech.iti0301.states;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import ee.taltech.iti0301.game.BombermanGdxGame;
import ee.taltech.iti0301.game.character.User;
import ee.taltech.iti0301.game.sounds.SoundHandler;
import ee.taltech.iti0301.network.events.GameStart;
import ee.taltech.iti0301.network.events.CurrentState;
import ee.taltech.iti0301.network.handlers.LobbyHandler;
import ee.taltech.iti0301.textures.BombermanTexture;

import javax.swing.JOptionPane;
import java.util.List;

public class LobbyState extends State {
    private List<User> players;
    private final Stage stageTable;
    private final Stage stageStartButton;
    private final SoundHandler soundHandler;
    public LobbyState(StateController sc) {
        super(sc);
        stageTable = new Stage();
        stageStartButton = new Stage();
        Gdx.input.setInputProcessor(stageStartButton);
        TextButton startButton = createStartButton();
        stageStartButton.addActor(startButton);
        soundHandler = new SoundHandler();
        soundHandler.playBomberManArea2();
    }

    public void checkActiveState() {
        if (LobbyHandler.INSTANCE.getGameServerStatus() && LobbyHandler.INSTANCE.getGameCount() == 0) {
            JOptionPane.showMessageDialog(null, "Try again later!", "Message", JOptionPane.PLAIN_MESSAGE);
            sc.setState(States.CONNECTSTATE);
        }
    }

    @Override
    public void update(float deltaTime) {
        checkActiveState();
        stageTable.clear();
        players = LobbyHandler.INSTANCE.getUsers();
        stageTable.addActor(createPlayerTable(players));

    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0.54f, 0.023f, 1);

        stageTable.act(Gdx.graphics.getDeltaTime());
        stageStartButton.act(Gdx.graphics.getDeltaTime());
        stageTable.draw();
        stageStartButton.draw();
    }

    private Table createPlayerTable(List<User> players) {
        final float tableWidth = 600;
        final float tableHeight = 800;
        Table resultTable = new Table();
        resultTable.setSize(tableWidth, tableHeight);
        resultTable.setPosition((float) Gdx.graphics.getWidth() / 2 - tableWidth / 2,
                (float) Gdx.graphics.getHeight() / 2 - 100);

        Label.LabelStyle labelStyle = new Label.LabelStyle();

        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts\\Roboto-Black.fnt"));
        font.getData().setScale(0.5f);
        labelStyle.font = font;

        Label playerName = new Label("Player name", labelStyle);
        resultTable.add(playerName).expandX().align(Align.left);
        resultTable.add().expandX().align(Align.left);

        for (User player : players) {
            resultTable.row(); // adds new row to the table
            Label playerLabel = new Label(player.getName(), labelStyle);
            resultTable.add(playerLabel).expandX().align(Align.left);
        }

        return resultTable;
    }

    /**
     * Create start button for the lobby
     * @return start button
     */
    private TextButton createStartButton() {
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts\\Roboto-Black.fnt"));
        font.getData().setScale(0.5f);

        TextButton.TextButtonStyle buttonStyle = createGreenButtonStyle();
        buttonStyle.font = font;

        TextButton startButton = new TextButton("Start", buttonStyle);
        startButton.setPosition((float)Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() - 800);
        startButton.setSize(200, 50);
        // Add click listener to the button
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameStart gameStart = new GameStart();
                BombermanGdxGame.client.sendUDP(gameStart);
                CurrentState currentState = new CurrentState();
                currentState.gameStatus = true;
                LobbyHandler.INSTANCE.incrementGameCount();
                BombermanGdxGame.client.sendUDP(currentState);
                soundHandler.stopBomberManArea2();
                sc.setState(States.PLAYSTATE);
            }
        });
        return startButton;
    }

    /**
     * create a green button with bitmapFont
     * @return the style
     */
    private TextButton.TextButtonStyle createGreenButtonStyle() {
        Skin skin = new Skin();
        skin.add("button", BombermanTexture.getGreenButtonTexture());
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();

        buttonStyle.up = skin.getDrawable("button");
        return buttonStyle;
    }

    @Override
    public void dispose() {
        stageTable.dispose();
        stageStartButton.dispose();
    }
}
