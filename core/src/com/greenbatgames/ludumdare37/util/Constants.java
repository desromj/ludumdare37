package com.greenbatgames.ludumdare37.util;

import com.badlogic.gdx.Input;

/**
 * Created by Quiv on 09-12-2016.
 */

public class Constants {

    private Constants() {}

    // Aspect ratios
    public static final float WORLD_WIDTH = 800f;
    public static final float WORLD_HEIGHT = WORLD_WIDTH * 4f / 5f;

    // User controls
    public static int KEY_QUIT = Input.Keys.ESCAPE;
    public static int KEY_RESTART = Input.Keys.R;

    public static int KEY_RIGHT = Input.Keys.RIGHT;
    public static int KEY_LEFT = Input.Keys.LEFT;
    public static int KEY_JUMP = Input.Keys.Z;
    public static int KEY_ATTACK = Input.Keys.X;

    // Physics Values
    public static final float PTM = 10f;

    // Player Values
    public static final float PLAYER_RADIUS = 6f;
    public static final float PLAYER_JUMP_RECOVERY = 0.25f; // time before able to jump again
    public static final float PLAYER_DENSITY = 1000f;
    public static final float PLAYER_GROUND_FRICTION = 1.0f;

    public static final float PLAYER_MIN_CLIMB_RATIO = 0.5f;    // climb between min and max percent
    public static final float PLAYER_MAX_CLIMB_RATIO = 1.0f;
    public static final float PLAYER_CLIMB_TIME = 1.0f;


}