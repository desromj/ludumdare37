package com.greenbatgames.ludumdare37.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.ludumdare37.DareGame;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by arne on 10-12-2016.
 */

public class RestartScreen extends ScreenAdapter {
    public static final String TAG = RestartScreen.class.getSimpleName();

    Viewport viewport;
    ShapeRenderer renderer;
    SpriteBatch batch;
    BitmapFont font;

    String text;

    public RestartScreen(){
        viewport = new ExtendViewport(
                Constants.WORLD_WIDTH,
                Constants.WORLD_HEIGHT
        );
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();

        text = "YOU DIED, PRESS 'R' TO RESTART";
    }

    @Override
    public void render(float delta){
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            DareGame.setScreen(GameScreen.class);
            return;
        }

        viewport.apply();

        // Start with a black background
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.end();

        // Move on to sprites and fonts
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.setColor(1, 1, 1, 1);

        font.setColor(1, 1, 1, 1);
        font.draw(
                batch,
                text,
                0f,
                viewport.getWorldHeight() * 10.0f / 16.0f,
                viewport.getWorldWidth(),
                Align.center,
                false);

        batch.end();
    }



    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height, true);
    }



    @Override
    public void dispose()
    {
        batch.dispose();
        font.dispose();
    }
}
