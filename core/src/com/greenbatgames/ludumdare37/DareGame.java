package com.greenbatgames.ludumdare37;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.I18NBundle;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.screen.Score;
import com.greenbatgames.ludumdare37.screen.ScoreScreen;
import com.greenbatgames.ludumdare37.screen.StartScreen;
import com.greenbatgames.ludumdare37.screen.TopScoreScreen;

public class DareGame extends Game {

    private static Game instance;
    private static I18NBundle bundle;
    public static Score score;
    public static boolean touchEnabled;

    Music music;

    @Override
    public void create ()
    {
        instance = this;
        score = new Score();
        touchEnabled = Gdx.input.isPeripheralAvailable(Input.Peripheral.MultitouchScreen);

        bundle = I18NBundle.createBundle(
                Gdx.files.internal("strings/MyBundle"),
                "ISO-8859-1"
        );

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/LD37music.mp3"));
        music.setLooping(true);
        music.play();

        DareGame.setScreen(StartScreen.class);
    }

    public static void setScreen(Class type)
    {
        if (type == GameScreen.class) {
            instance.setScreen(GameScreen.getInstance());
            GameScreen.getInstance().init();
        } else if (type == StartScreen.class) {
            instance.setScreen(new StartScreen(
                    "LGG_logo.png",
                    getString("game"),
                    getString("subtitle"),
                    0.8f));
        } else if (type == ScoreScreen.class) {
            instance.setScreen(new ScoreScreen());
        } else if (type == TopScoreScreen.class) {
            instance.setScreen(new TopScoreScreen());
        }
    }

    public static Score score() { return score; }

    public static String getString(String key, String ... args) {
        return bundle.format(key, args);
    }
}
