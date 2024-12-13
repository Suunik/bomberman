package ee.taltech.iti0301.game.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class CharacterNameHandler {
    private BitmapFont nameFont;
    private final GlyphLayout layout;

    public CharacterNameHandler() {
        layout = new GlyphLayout();
    }

    /**
     * draws the characters name on top of the character
     * @param batch to draw the name onto
     * @param character to draw the name on top of
     */
    public void drawCharacterName(SpriteBatch batch, Character character) {
        if (nameFont == null) {
            nameFont = assignFont();
        }
        Vector2 characterPos = character.getPosition();
        String username = character.getUsername();

        layout.setText(nameFont, username);
        float textWidth = layout.width;
        float textHeight = layout.height;

        float x = characterPos.x - textWidth / 2 + character.getCharacterWidth() / 2; // Center the name horizontally

        float y = characterPos.y + character.getCharacterHeight() * 1.3f + textHeight / 2;

        nameFont.draw(batch, username, x, y);

    }

    /**
     *
     * @return bitmap font
     */
    private BitmapFont assignFont() {
        BitmapFont font = new BitmapFont(Gdx.files.internal("fonts\\Roboto-Black.fnt"));
        font.getData().setScale(0.5f);
        return font;
    }
    public void dispose() {
        nameFont.dispose();
    }
}
