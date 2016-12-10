package com.greenbatgames.ludumdare37;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.screen.RestartScreen;
import com.greenbatgames.ludumdare37.screen.StartScreen;

public class DareGame extends Game {

    private static Game instance;

    @Override
    public void create ()
    {
        instance = this;

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
                    "TENTATIVE TITLE",
                    "Game for Ludum Dare 37\nBy Arne 'S Jegers and Mike Desrochers",
                    0.8f));
        } else if(type == RestartScreen.class){
            instance.setScreen(new RestartScreen());
        }
    }
}
