package ee.taltech.iti0301.textures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class BombermanTexture {
    /**
     * holds all the textures of the bomberman game that can be accessed via getter methods
     * dispose method disposes all the textures
     */
    private BombermanTexture() {

    }
    private static final Texture bomberManSheet =
            new Texture(Gdx.files.internal("bomberman_sheet.png"));
    private static final Texture bombSheet =
            new Texture(Gdx.files.internal("bomb\\bomb_sheet.png"));
    private static final Texture explosionPowerUpTexture =
            new Texture(Gdx.files.internal("power_ups\\flame_power_up.png"));
    private static final Texture bombPowerUpTexture =
            new Texture(Gdx.files.internal("power_ups\\bomb_power_up.png"));
    private static final Texture speedPowerUpTexture =
            new Texture(Gdx.files.internal("power_ups\\speed_power_up.png"));
    private static final Texture indestructibleTileTexture =
            new Texture(Gdx.files.internal("unbreakableWall\\stone.png"));
    private static final Texture destructibleTileTexture =
            new Texture(Gdx.files.internal("breakableWall\\crate.png"));
    private static final Texture explosionTexture =
            new Texture((Gdx.files.internal("explosions\\explosion.png")));
    private static final Texture greenButtonTexture =
            new Texture(Gdx.files.internal("button\\button.png"));
    private static final Texture readyCheckMark = new Texture(Gdx.files.internal("readyMark\\check.png"));
    private static final Texture bombermanGameLogo = new Texture(Gdx.files.internal("logos\\bomberman_logo.png"));
    public static Texture getIndestructibleTileTexture() {
        return indestructibleTileTexture;
    }
    public static Texture getDestructibleTileTexture() {
        return destructibleTileTexture;
    }

    public static Texture getBomberManSheet() {
        return bomberManSheet;
    }

    public static Texture getBombPowerUpTexture() {
        return bombPowerUpTexture;
    }

    public static Texture getBombSheet() {
        return bombSheet;
    }

    public static Texture getExplosionPowerUpTexture() {
        return explosionPowerUpTexture;
    }

    public static Texture getExplosionTexture() {
        return explosionTexture;
    }

    public static Texture getSpeedPowerUpTexture() {
        return speedPowerUpTexture;
    }

    public static Texture getGreenButtonTexture() {
        return greenButtonTexture;
    }

    public static Texture getBombermanGameLogo() {
        return bombermanGameLogo;
    }

    /**
     * dispose all the textures
     */
    public static void dispose() {
        bomberManSheet.dispose();
        bombSheet.dispose();
        bombPowerUpTexture.dispose();
        explosionPowerUpTexture.dispose();
        speedPowerUpTexture.dispose();
        destructibleTileTexture.dispose();
        indestructibleTileTexture.dispose();
        explosionTexture.dispose();
        greenButtonTexture.dispose();
        readyCheckMark.dispose();
    }
}
