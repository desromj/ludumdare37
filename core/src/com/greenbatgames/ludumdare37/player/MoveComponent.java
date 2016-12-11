package com.greenbatgames.ludumdare37.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.ludumdare37.util.Constants;
import com.greenbatgames.ludumdare37.util.DareSounds;

/**
 * Created by Quiv on 10-12-2016.
 */

public class MoveComponent extends PlayerComponent {

    public static final String TAG = MoveComponent.class.getSimpleName();

    private int numFootContacts;
    private boolean doubleJumped, facingRight;
    private float cannotJumpFor, disableCollisionFor;

    private float cannotDashFor, dashCooldown;

    private float walkSoundTimer;

    public MoveComponent(Player player) {
        super(player);
    }



    @Override
    public void init() {
        numFootContacts = 0;
        doubleJumped = false;
        facingRight = true;
        cannotJumpFor = 0.0f;
        disableCollisionFor = 0.0f;
        cannotDashFor = 0.0f;
        dashCooldown = 0.0f;
        walkSoundTimer = 0;
    }



    @Override
    public boolean update(float delta)
    {
        cannotJumpFor -= delta;
        disableCollisionFor -= delta;
        cannotDashFor -= delta;
        dashCooldown -= delta;

        // Horizontal hopping movement
        Body body = player.getBody();

        // check to see if we should dash first
        if (canDash()) {
            cannotDashFor = Constants.PLAYER_DASH_DURATION;
            dashCooldown = Constants.PLAYER_DASH_COOLDOWN;
        }

        // Handle dashing, until finished
        if (isDashing()) {
            if (facingRight) {
                body.setLinearVelocity(
                        Constants.PLAYER_DASH_SPEED,
                        body.getLinearVelocity().y);
            } else {
                body.setLinearVelocity(
                        -Constants.PLAYER_DASH_SPEED,
                        body.getLinearVelocity().y);
            }
        } else if (player.isWalkButtonHeld()) {
            walkSoundTimer -= delta;
            if(walkSoundTimer < 0){
                walkSoundTimer = Constants.WALK_SOUND_TIME;
                int s = MathUtils.floor(MathUtils.random(1, 4.99f));
                if(s == 1){
                    DareSounds.STEP1.play();
                } else if(s == 2){
                    DareSounds.STEP2.play();
                } else if(s == 3){
                    DareSounds.STEP3.play();
                } else if(s == 4){
                    DareSounds.STEP4.play();
                }
            }
            // Handle movement (left/right)
            if (Gdx.input.isKeyPressed(Constants.KEY_RIGHT)) {
                body.setLinearVelocity(
                        Constants.PLAYER_MOVE_SPEED,
                        body.getLinearVelocity().y);
                facingRight = true;
            } else if (Gdx.input.isKeyPressed(Constants.KEY_LEFT)) {
                body.setLinearVelocity(
                        -Constants.PLAYER_MOVE_SPEED,
                        body.getLinearVelocity().y);
                facingRight = false;
            }
        } else {
            walkSoundTimer = 0;
            body.setLinearVelocity(
                    body.getLinearVelocity().x * Constants.HORIZONTAL_MOVE_DAMPEN,
                    body.getLinearVelocity().y);
        }

        // Add impulse for single jump
        if (canJump() && Gdx.input.isKeyJustPressed(Constants.KEY_JUMP)) {

            body.setLinearVelocity(
                    body.getLinearVelocity().x,
                    0f
            );

            body.applyLinearImpulse(
                Constants.PLAYER_JUMP_IMPULSE,
                body.getPosition(),
                true
            );

            if (numFootContacts == 0)
                doubleJumped = true;
        }

        return true;
    }

    /*
        Getters and Setters
     */

    public void incNumFootContacts() {

        if (numFootContacts == 0)
            doubleJumped = false;

        numFootContacts++;
    }

    public void decNumFootContacts() {
        numFootContacts--;
    }

    public boolean canJump() {
        return (cannotJumpFor <= 0f && numFootContacts > 0)
                || !doubleJumped;
    }

    public boolean isCollisionDisabled() {
        return disableCollisionFor > 0f;
    }

    public boolean isOnGround() { return numFootContacts > 0; }
    public boolean isInAir() { return numFootContacts <= 0; }
    public boolean isFacingRight() { return facingRight; }
    public boolean isDashing() { return cannotDashFor >= 0f; }
    
    public boolean canDash() {
        return
                player.getBody().getLinearVelocity().x != 0
                && Gdx.input.isKeyJustPressed(Constants.KEY_ATTACK)
                && dashCooldown < 0.0f;
    }
}
