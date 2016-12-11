package com.greenbatgames.ludumdare37.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.ludumdare37.screen.GameScreen;

/**
 * Created by Quiv on 11-12-2016.
 */

public class Background {

    SpriteBatch batch;
    Sprite sprite;

    public Background(String resource) {
        sprite = new Sprite(new Texture(Gdx.files.internal(resource)));
        batch = new SpriteBatch();
    }

    public void render() {
        Viewport viewport = GameScreen.level().getViewport();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);    // Black background
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(
                sprite,
                0,
                0,
                0,
                0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight(),
                1.2f,
                1.2f,
                0f
        );
        batch.end();
    }
}
