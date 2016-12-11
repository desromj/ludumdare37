package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by arne on 10-12-2016.
 */

public class GoonMoveComponent implements Initializable {

    Goon goon;
    boolean facingRight;

    private enum State {
        WALK,
        WAIT
    }

    //TODO: Put this in an enum instead
    private State currentState;
    private float stateTimer;

    // 1 is right, -1 is left
    private float walkingSpeed;

    GoonMoveComponent(Goon goon){
        this.goon = goon;

        init();
    }

    public void init(){
        currentState = State.WALK;
        stateTimer = Constants.GOON_WAIT_TIME;
        walkingSpeed = Constants.GOON_MOVE_SPEED;
        facingRight = true;
    }

    public void update(float delta){

        if (goon.getParentPlatform() == null) return;
        stateTimer -= delta;

        Body body = goon.getBody();

        //Walking
        if(currentState == State.WALK){

            // If we are walking right, check for the right edge of the platform
            float newX = walkingSpeed;

            if (walkingSpeed > 0) {

                if (goon.getRight() >= goon.getParentPlatform().getRight()) {
                    newX = 0;
                } else {
                    newX = walkingSpeed;
                }
                facingRight = true;
            } else if (walkingSpeed < 0) {
                if (goon.getLeft() <= goon.getParentPlatform().getLeft()) {
                    newX = 0;
                } else {
                    newX = walkingSpeed;
                }
                facingRight = false;
            }

            body.setLinearVelocity(
                    newX,
                    body.getLinearVelocity().y);

            if(stateTimer <= 0){
                currentState = State.WAIT;
                stateTimer = Constants.GOON_WAIT_TIME;
            }

        //Standing still
        } else if(currentState == State.WAIT){
            body.setLinearVelocity(
                    0f,
                    body.getLinearVelocity().y);
            if(stateTimer <= 0){
                currentState = State.WALK;
                walkingSpeed = -walkingSpeed;
                stateTimer = Constants.GOON_WALK_TIME;
            }
        }
    }

    public boolean isFacingRight() {
        return facingRight;
    }

}
