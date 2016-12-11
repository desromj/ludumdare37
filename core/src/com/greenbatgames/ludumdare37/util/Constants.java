package com.greenbatgames.ludumdare37.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Quiv on 09-12-2016.
 */

public class Constants {

    private Constants() {}

    // Aspect ratios
    public static final float WORLD_WIDTH = 1600;
    public static final float WORLD_HEIGHT = WORLD_WIDTH * 4f / 5f;

    public static final float TILE_WIDTH = 32f; // Tiles are 32x32 pixels

    // User controls
    public static int KEY_QUIT = Input.Keys.ESCAPE;
    public static int KEY_RESTART = Input.Keys.R;

    public static int KEY_RIGHT = Input.Keys.RIGHT;
    public static int KEY_LEFT = Input.Keys.LEFT;
    public static int KEY_JUMP = Input.Keys.Z;
    public static int KEY_ATTACK = Input.Keys.X;

    // Physics Values
    public static final float PTM = 40f;
    public static final Vector2 GRAVITY = new Vector2(0f, -75f);

    // Player Values
    public static final float PLAYER_RADIUS = 16f;
    public static final float PLAYER_JUMP_RECOVERY = 0.25f; // time before able to jump again
    public static final float PLAYER_DENSITY = 1000f;
    public static final float PLAYER_GROUND_FRICTION = 0f;

    public static final float PLAYER_MIN_CLIMB_RATIO = 0.5f;    // climb between min and max percent
    public static final float PLAYER_MAX_CLIMB_RATIO = 1.0f;
    public static final float PLAYER_CLIMB_TIME = 0.5f;

    public static final float PLAYER_MOVE_SPEED = WORLD_WIDTH / 160f;
    public static final float PLAYER_DASH_SPEED = PLAYER_MOVE_SPEED * 5.0f;
    public static final float PLAYER_DASH_DURATION = 0.1f;
    public static final float PLAYER_DASH_COOLDOWN = 1.3f;

    public static final Vector2 PLAYER_JUMP_IMPULSE = new Vector2(0f,
            2.2f * PLAYER_RADIUS * PLAYER_DENSITY);

    public static final float HORIZONTAL_MOVE_DAMPEN = 0.5f;

    //Ghost Values
    public static final float GHOST_MOVE_SPEED = WORLD_WIDTH / 256f;

    //Goon Values
    public static final float GOON_MOVE_SPEED = WORLD_WIDTH / 160f;
    public static final float GOON_WAIT_TIME = 2f;
    public static final float GOON_WALK_TIME = 3f;

    //Poison Gas Values
    public static final float GAS_DURATION = 30f;

    //Pressure Plate Values
    public static final float PRESSURE_PLATE_HEIGHT = 10f;
    public static final float PRESSURE_PLATE_DURATION = 2f;
    public static final float PRESSURE_PLATE_EXPLOSION_RADIUS = 150f;

    //Turret Values
    public static final float TURRET_RANGE = 300f;
    public static final float TURRET_ANG_RADIUS = 0.5f;
    public static final float TURRET_WAIT_TIME = 1.5f;
    public static final float TURRET_AIM_TIME = 1f;
    public static final float TURRET_ROTATION_SPEED = MathUtils.PI/4;
    public static final float TURRET_MIN_ANGLE = 0;
    public static final float TURRET_MAX_ANGLE = MathUtils.PI;

    // HUD values
    public static final float RESTART_FONT_SCALE = 6.0f;
    public static final Color RESTART_FONT_COLOR = Color.BLACK;
}
