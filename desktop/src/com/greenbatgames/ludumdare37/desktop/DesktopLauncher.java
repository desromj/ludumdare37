package com.greenbatgames.ludumdare37.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.greenbatgames.ludumdare37.DareGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        config.width = 1200;
        config.height = 900;

		new LwjglApplication(new DareGame(), config);
	}
}
