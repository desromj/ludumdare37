package com.greenbatgames.ludumdare37.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

public class MoveComponent extends PlayerComponent {

    public static final String TAG = MoveComponent.class.getSimpleName();

    private boolean grounded, jumped, facingRight;
    private float cannotJumpFor, disableCollisionFor;

    public MoveComponent(Player player) {
        super(player);
    }



    @Override
    public void init() {
        jumped = false;
        grounded = true;
        facingRight = true;
        cannotJumpFor = 0.0f;
        disableCollisionFor = 0.0f;
    }



    @Override
    public boolean update(float delta)
    {
        cannotJumpFor -= delta;
        disableCollisionFor -= delta;

        // First handle recovery time for landing
        if (!canJump()) return true;

        // Horizontal hopping movement
        Body body = player.getBody();

        // Then determine what to do based on if we're in the air or one the ground
        if (isOnGround()) {

        }

        // Limit velocity based on walking, running, jumping, etc
        if (player.isWalkButtonHeld()) {

        }

        return true;
    }



    public void land() {

        Gdx.app.log(TAG, "Land triggered");

        if (grounded && !jumped) {
            Gdx.app.log(TAG, "Already landed, ignored");
            return;
        }

        grounded = true;
        jumped = false;

        cannotJumpFor = Constants.PLAYER_JUMP_RECOVERY;
        // set next player.animator() values
    }



    public void jump() {
        // cannot jump if we already jumped
        if (jumped) return;

        // Otherwise, proceed with jump
        jumped = true;
        grounded = false;
    }

    /*
        Getters and Setters
     */

    public boolean canJump() {
        return cannotJumpFor <= 0f;
    }

    public boolean isCollisionDisabled() {
        return disableCollisionFor > 0f;
    }

    public boolean isOnGround() { return grounded && !jumped; }
    public boolean isInAir() { return !grounded; }
    public boolean isFacingRight() { return facingRight; }
}
