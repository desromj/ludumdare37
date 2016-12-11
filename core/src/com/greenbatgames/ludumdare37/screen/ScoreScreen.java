package com.greenbatgames.ludumdare37.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.ludumdare37.DareGame;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 11-12-2016.
 */

public class ScoreScreen extends ScreenAdapter {

    Viewport viewport;

    SpriteBatch batch;
    ShapeRenderer renderer;
    BitmapFont font;

    public ScoreScreen() {
        viewport = new ExtendViewport(
                Constants.WORLD_WIDTH,
                Constants.WORLD_HEIGHT);

        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(Constants.SCORE_SCREEN_FONT_SCALE);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    @Override
    public void render(float delta) {

        // Go back to start screen on any input
        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            DareGame.score().reset();
            DareGame.setScreen(StartScreen.class);
        }

        viewport.apply();

        // Start with a black background
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.end();

        // Draw lighter text

        // Move on to sprites and fonts
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        font.setColor(Constants.SCORE_SCREEN_FONT_COLOR);

        // Title
        font.draw(
                batch,
                "Scores",
                viewport.getWorldWidth() * 0.5f,
                viewport.getWorldHeight() * 0.9f,
                0f,
                Align.center,
                false
        );

        // Draw level numbers
        for (int i = 0; i < DareGame.score().getLevels().size(); i++) {
            font.draw(
                    batch,
                    "Level " + DareGame.score().getLevels().get(i) + "   :  ",
                    viewport.getWorldWidth() * 0.5f,
                    viewport.getWorldHeight() * 0.9f - ((2+i) * Constants.SCORE_FONT_SPACING),
                    0f,
                    Align.right,
                    false
            );
        }

        // Draw scores
        for (int i = 0; i < DareGame.score().getScores().size(); i++) {
            font.draw(
                    batch,
                    "   " + DareGame.score().getScores().get(i) + " sec",
                    viewport.getWorldWidth() * 0.5f,
                    viewport.getWorldHeight() * 0.9f - ((2+i) * Constants.SCORE_FONT_SPACING),
                    0f,
                    Align.left,
                    false
            );
        }

        // Draw records beat, only if top score is beat
        for (int i = 0; i < DareGame.score().getScores().size(); i++) {
            font.setColor(Constants.SCORE_RECORD_COLOR);

            if (DareGame.score().beatTopScore(i)) {
                font.draw(
                        batch,
                        " * * * NEW RECORD * * * ",
                        viewport.getWorldWidth() * 0.6f,
                        viewport.getWorldHeight() * 0.9f - ((2 + i) * Constants.SCORE_FONT_SPACING),
                        0f,
                        Align.left,
                        false
                );
            }
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
