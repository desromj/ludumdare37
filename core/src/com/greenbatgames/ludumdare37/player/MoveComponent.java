package com.greenbatgames.ludumdare37.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

public class MoveComponent extends PlayerComponent {

    public static final String TAG = MoveComponent.class.getSimpleName();

    private int numFootContacts;
    private boolean doubleJumped, facingRight;
    private float cannotJumpFor, disableCollisionFor;

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
    }



    @Override
    public boolean update(float delta)
    {
        cannotJumpFor -= delta;
        disableCollisionFor -= delta;

        // Horizontal hopping movement
        Body body = player.getBody();

        // Handle movement (left/right)
        if (player.isWalkButtonHeld()) {

            if (Gdx.input.isKeyPressed(Constants.KEY_RIGHT)) {
                body.setLinearVelocity(
                        Constants.PLAYER_MOVE_SPEED,
                        body.getLinearVelocity().y);
            } else if (Gdx.input.isKeyPressed(Constants.KEY_LEFT)) {
                body.setLinearVelocity(
                        -Constants.PLAYER_MOVE_SPEED,
                        body.getLinearVelocity().y);
            }
        } else {
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
}
