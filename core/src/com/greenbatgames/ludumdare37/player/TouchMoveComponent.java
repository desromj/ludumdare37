package com.greenbatgames.ludumdare37.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.ludumdare37.util.Constants;
import com.greenbatgames.ludumdare37.util.DareSounds;

/**
 * Created by Quiv on 17-12-2016.
 */

public class TouchMoveComponent extends KeyboardMoveComponent {

    public static final String TAG = TouchMoveComponent.class.getSimpleName();

    public TouchMoveComponent(Player player) {
        super(player);
    }

    // TODO: Replace keyboard controls in update() with touch controls
    @Override
    public boolean update(float delta) {
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
                        (body.getLinearVelocity().y >= 0f) ? body.getLinearVelocity().y : 0f);
            } else {
                body.setLinearVelocity(
                        -Constants.PLAYER_DASH_SPEED,
                        (body.getLinearVelocity().y >= 0f) ? body.getLinearVelocity().y : 0f);
            }
        } else if (player.isWalkButtonHeld()) {
            walkSoundTimer -= delta;
            if(isOnGround() && walkSoundTimer < 0){
                walkSoundTimer = Constants.WALK_SOUND_TIME;
                int s = MathUtils.floor(MathUtils.random(1, 4.99f));
                if(s == 1){
                    walkSound1.play(DareSounds.STEP1.getVolume());
                } else if(s == 2){
                    walkSound2.play(DareSounds.STEP2.getVolume());
                } else if(s == 3){
                    walkSound3.play(DareSounds.STEP3.getVolume());
                } else if(s == 4){
                    walkSound4.play(DareSounds.STEP4.getVolume());
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

    // TODO: Touch controls
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

}
