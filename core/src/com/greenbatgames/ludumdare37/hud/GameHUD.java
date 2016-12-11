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
 * Created by Quiv on 10-12-2016.
 */

// Any required HUD goes here for normal gameplay
public class GameHUD extends Actor implements Initializable {
    public static final String TAG = GameHUD.class.getSimpleName();

    BitmapFont font;
    long startTime;
    float elapsedTime;
    boolean stopTimer;

    public GameHUD() {
        font = new BitmapFont();

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(Constants.RESTART_FONT_SCALE);

        init();
    }

    @Override
    public void init() {
        startTime = TimeUtils.nanoTime();
        stopTimer = false;
    }

    @Override
    public void act(float delta) {
        if (!stopTimer)
            elapsedTime = TimeUtils.nanosToMillis((TimeUtils.nanoTime() - startTime)) / 1000f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // Move on to sprites and fonts
        Viewport viewport = GameScreen.level().getViewport();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        font.setColor(Constants.RESTART_FONT_COLOR);
        font.draw(
                batch,
                "Time: " + elapsedTime + " sec\n" +
                "Current Level: " + GameScreen.getInstance().getCurrentLevel(),
                Constants.HUD_MARGIN,
                viewport.getWorldHeight() - Constants.HUD_MARGIN,
                0f,
                Align.topLeft,
                false);

        // Draw if the time is a new record
        if (stopTimer && !GameScreen.level().getPlayer().isDead()) {

            font.setColor(Constants.SCORE_RECORD_COLOR);
            if (DareGame.score().beatTopScore(GameScreen.getInstance().getCurrentLevel() - 1)) {
                font.draw(
                        batch,
                        " * * * NEW RECORD * * * ",
                        viewport.getWorldWidth() / 2f,
                        viewport.getWorldHeight() - Constants.HUD_MARGIN,
                        0f,
                        Align.center,
                        false
                );
            }
        }
    }

    public void setStopTimer(boolean val) {
        stopTimer = val;
    }

    public float getElapsedTime() { return elapsedTime; }
}
