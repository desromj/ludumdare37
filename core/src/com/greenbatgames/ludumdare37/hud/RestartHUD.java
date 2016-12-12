package com.greenbatgames.ludumdare37.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

// Draw the "YOU DIED, Press R to restart" line here
public class RestartHUD extends Actor implements Initializable {
    public static final String TAG = RestartHUD.class.getSimpleName();

    BitmapFont font;

    String text;
    boolean visible;

    public RestartHUD() {
        font = new BitmapFont();

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(Constants.RESTART_FONT_SCALE);
        font.setColor(Constants.MAIN_FONT_COLOR);

        text = "YOU DIED, PRESS 'R' TO RESTART";
        visible = false;
    }

    @Override
    public void init() {
        visible = false;
    }

    @Override
    public void act(float delta) {
        if (!visible) return;

        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            GameScreen.getInstance().reloadCurrentLevel();
            return;
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        if (!visible) return;

        // Move on to sprites and fonts
        Viewport viewport = GameScreen.level().getViewport();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        font.draw(
                batch,
                text,
                viewport.getWorldWidth() / 2.0f,
                viewport.getWorldHeight() * 10.0f / 16.0f,
                0f,
                Align.center,
                false);
    }

    public void show() {
        visible = true;
    }
    public boolean isVisible() { return visible; }
}
