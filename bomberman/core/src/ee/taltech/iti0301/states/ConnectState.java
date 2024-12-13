package ee.taltech.iti0301.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import ee.taltech.iti0301.game.BombermanGdxGame;
import ee.taltech.iti0301.game.sounds.SoundHandler;
import ee.taltech.iti0301.network.events.JoinRequest;
import ee.taltech.iti0301.network.handlers.PlayerHandler;
import ee.taltech.iti0301.textures.BombermanTexture;

public class ConnectState extends State {
    private Integer width = Gdx.graphics.getWidth();
    private Integer height = Gdx.graphics.getHeight();
    private final Stage stage;
    private final BitmapFont connectFont;
    private final BitmapFont exitFont;
    private final TextField username;
    public static String input;
    private final Texture logo;
    private final SoundHandler soundHandler;

    protected ConnectState(StateController sc) {
        super(sc);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        connectFont = new BitmapFont(Gdx.files.internal("fonts\\button_font.fnt"));
        exitFont = new BitmapFont(Gdx.files.internal("fonts\\button_font.fnt"));
        logo = BombermanTexture.getBombermanGameLogo();
        TextButton connect = createConnectButton();
        TextButton exitButton = createExitButton();
        username = createTextField();
        stage.addActor(connect);
        stage.addActor(exitButton);
        stage.addActor(username);
        Gdx.input.setInputProcessor(stage);
        soundHandler = new SoundHandler();
        soundHandler.playTitleMusic();
    }

    private TextField createTextField() {
        final Skin fieldSkin = new Skin(Gdx.files.internal("uiskin.json"));
        TextField username = new TextField("Username", fieldSkin);
        username.setSize(250, 50);
        username.setAlignment(1);
        username.setPosition((float)Gdx.graphics.getWidth() / 2 - 125, (float)Gdx.graphics.getHeight() / 2 - 35);
        return username;
    }

    private TextButton createConnectButton() {
        connectFont.getData().setScale(1.0f, 1.0f);

        TextButton.TextButtonStyle buttonStyle = createGreenButtonStyle();
        buttonStyle.font = connectFont;

        TextButton startButton = new TextButton("CONNECT", buttonStyle);
        startButton.setPosition((float)Gdx.graphics.getWidth() / 2 - 125, (float)Gdx.graphics.getHeight() / 2 - 145);
        startButton.setSize(250, 70);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JoinRequest joinRequest = new JoinRequest();
                joinRequest.username = input;
                PlayerHandler.INSTANCE.setUsername(input);
                BombermanGdxGame.client.sendTCP(joinRequest);
                soundHandler.stopTitleMusic();
                sc.setState(States.MENUSTATE);
            }
        });
        return startButton;
    }

    private TextButton createExitButton() {
        exitFont.getData().setScale(0.6f, 0.6f);
        TextButton.TextButtonStyle buttonStyle = createGreenButtonStyle();
        buttonStyle.font = exitFont;
        TextButton exit = new TextButton("Quit game", buttonStyle);
        exit.setPosition((float)Gdx.graphics.getWidth() / 2 - 82.5f, (float)Gdx.graphics.getHeight() / 2 - 215);
        exit.setSize(165, 55);
        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                BombermanGdxGame.client.close();
            }
        });
        return exit;
    }

    private TextButton.TextButtonStyle createGreenButtonStyle() {
        Skin skin = new Skin();
        skin.add("button", BombermanTexture.getGreenButtonTexture());
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();

        buttonStyle.up = skin.getDrawable("button");
        return buttonStyle;
    }


    @Override
    public void update(final float deltaTime) {
        input = username.getText();
    }

    @Override
    public void render() {
        ScreenUtils.clear(1.0f, 0.5f, 0f, 0f);

        batch.begin();
        batch.draw(logo, 75, (float)height / 2,
                (float)width / 2 + (float)width / 3, (float)height / 2 - (float)height / 8);
        batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        logo.dispose();
    }
}
