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

    //Light values
    public static final float LIGHTS_AMBIENT_LEVEL = 0.2f;
    public static final float LIGHTS_FLICKER_OFF_TIME = 0.1f;
    public static final float LIGHTS_FLICKER_ON_TIME = 1f;

    //Ghost Values
    public static final float GHOST_MOVE_SPEED = WORLD_WIDTH / 256f;

    //Goon Values
    public static final float GOON_MOVE_SPEED = WORLD_WIDTH / 160f;
    public static final float GOON_WAIT_TIME = 2f;
    public static final float GOON_WALK_TIME = 3f;

    //Laser Grid Values
    public static final float LASERGRID_ON_TIME = 2f;
    public static final float LASERGRID_OFF_TIME = 3f;
    public static final float LASERGRID_WARMUP_TIME = 0.5f;

    //Lava Values
    public static final float LAVA_BUBBLE_LOW_TIME = 0.75f;
    public static final float LAVA_BUBBLE_HIGH_TIME = 1.25f;

    //Poison Gas Values
    public static final float GAS_DURATION = 30f;

    //Pressure Plate Values
    public static final float PRESSURE_PLATE_HEIGHT = 10f;
    public static final float PRESSURE_PLATE_DURATION = 0.5f;
    public static final float PRESSURE_PLATE_EXPLOSION_RADIUS = 150f;
    public static final float PRESSURE_PLATE_EXPLOSION_DURATION = 0.75f;

    //Sound Values
    public static final float GOON_WALK_SOUND_TIME = 0.35f;
    public static final float TURRET_BEEP_TIME = 2.0f;
    public static final float WALK_SOUND_TIME = 0.2f;

    //Turret Values
    public static final float TURRET_RANGE = 300f;
    public static final float TURRET_ANG_RADIUS = 30f;
    public static final float TURRET_WAIT_TIME = 1.5f;
    public static final float TURRET_AIM_TIME = 1f;
    public static final float TURRET_ROTATION_SPEED = MathUtils.PI/2;
    public static final float TURRET_ROTATION_SPEED_FAST = MathUtils.PI*3/4;
    public static final float TURRET_MIN_ANGLE = 0;
    public static final float TURRET_MAX_ANGLE = MathUtils.PI;

    // HUD values
    public static final Color MAIN_FONT_COLOR = Color.CYAN;

    public static final float GAME_HUD_FONT_SCALE = 1.5f;

    public static final float START_SCREEN_TITLE_SCALE = 2.0f;
    public static final float START_SCREEN_SUBTITLE_SCALE = 1.0f;

    public static final float RESTART_FONT_SCALE = 1.5f;
    public static final float HUD_MARGIN = WORLD_WIDTH / 200f;

    public static final float END_LEVEL_FONT_SCALE = 2.0f;

    public static final float SCORE_SCREEN_FONT_SCALE = 1.25f;
    public static final Color SCORE_RECORD_COLOR = Color.GOLD;
    public static final float SCORE_FONT_SPACING = 40f;

}
