package com.greenbatgames.ludumdare37.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.greenbatgames.ludumdare37.DareGame;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(750, 600);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new DareGame();
        }
}