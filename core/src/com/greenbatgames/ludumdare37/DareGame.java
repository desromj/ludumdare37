package com.greenbatgames.ludumdare37;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.screen.Score;
import com.greenbatgames.ludumdare37.screen.StartScreen;

public class DareGame extends Game {

    private static Game instance;
    public static Score score;

    Music music;

    @Override
    public void create ()
    {
        instance = this;
        score = new Score();

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/LD37.wav"));
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
                    "GBG-logo-shaded.png",
                    "A TO A",
                    "Game for Ludum Dare 37\nBy Arne 'S Jegers and Mike Desrochers",
                    0.8f));
        }
    }

    public static Score score() { return score; }
}
