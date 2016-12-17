package com.greenbatgames.ludumdare37.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.ludumdare37.DareGame;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 11-12-2016.
 */

public class EndLevelHUD extends Actor implements Initializable {
    public static final String TAG = EndLevelHUD.class.getSimpleName();

    BitmapFont font;

    boolean visible;

    public EndLevelHUD() {
        font = new BitmapFont(Gdx.files.internal("fonts/arial-grad.fnt"));

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(Constants.END_LEVEL_FONT_SCALE);
        font.setColor(Constants.MAIN_FONT_COLOR);

        init();
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
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            GameScreen.getInstance().nextLevel();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!visible) return;

        // Move on to sprites and fonts
        Viewport viewport = GameScreen.level().getViewport();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        font.draw(
                batch,
                DareGame.getString("pressToReplay"),
                Constants.HUD_MARGIN,
                viewport.getWorldHeight() / 1.4f,
                viewport.getWorldWidth() - Constants.HUD_MARGIN * 2f,
                Align.center,
                true);
    }

    public void show() {
        visible = true;
    }
    public boolean isVisible() { return visible; }

}
