package com.greenbatgames.ludumdare37.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.greenbatgames.ludumdare37.DareGame;
import com.greenbatgames.ludumdare37.level.Level;
import com.greenbatgames.ludumdare37.level.LevelLoader;
import com.greenbatgames.ludumdare37.level.Levels;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 09-12-2016.
 */

public class GameScreen extends ScreenAdapter implements InputProcessor {

    public static final String TAG = GameScreen.class.getSimpleName();
    private static GameScreen instance = null;

    private Level level;
    private Levels levelList;

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
        levelList = new Levels();
        level = LevelLoader.loadLevel(levelList.currentResource());
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        level.render(delta);
    }

    public void nextLevel() {

        level.dispose();

        if (levelList.hasNextLevel()) {
            level = LevelLoader.loadLevel(levelList.nextResource());
        } else {
            DareGame.setScreen(ScoreScreen.class);
        }
    }

    // Save the elapsed time for the current level somewhere here
    public void saveCurrentLevelTime() {
        DareGame.score().setScore(
                levelList.getCurrentLevelNumber(),
                level.getLevelElapsedTime());
    }

    public void reloadCurrentLevel() {
        level = LevelLoader.loadLevel(levelList.currentResource());
    }

    public int getCurrentLevel() {
        return levelList.getCurrentLevelNumber();
    }

    public int getTotalNumberLevels() { return levelList.size(); }

    public String currentTooltip() { return levelList.currentTooltip(); }
    public boolean hasCurrentTooltip() { return levelList.hasCurrentTooltip(); }


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
        return level().touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return level().touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return level().touchDragged(screenX, screenY, pointer);
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
