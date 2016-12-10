package com.greenbatgames.ludumdare37.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.greenbatgames.ludumdare37.DareGame;
import com.greenbatgames.ludumdare37.level.Level;
import com.greenbatgames.ludumdare37.level.LevelLoader;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 09-12-2016.
 */

public class GameScreen extends ScreenAdapter implements InputProcessor {

    public static final String TAG = GameScreen.class.getSimpleName();
    private static GameScreen instance = null;

    private Level level;

    private GameScreen() {}

    public static final GameScreen getInstance()
    {
        if (instance == null) {
            instance = new GameScreen();
            instance.init();
        }

        return instance;
    }

    public static final Level level() { return instance.level; }

    public void init() {
        level = LevelLoader.loadLevel("maps/map4.tmx");
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        level.render(delta);
    }



    /*
        ScreenAdapter Overrides
     */

    @Override
    public void resize(int width, int height)
    {
        level.getViewport().update(width, height, true);
    }

    /*
        InputProcessor Overrides
     */

    @Override
    public boolean keyDown(int keycode)
    {
        if (keycode == Constants.KEY_QUIT)
            Gdx.app.exit();
        else if (keycode == Constants.KEY_RESTART)
            DareGame.setScreen(StartScreen.class);

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
