package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.util.Constants;
import com.greenbatgames.ludumdare37.util.DareSounds;

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

    private State currentState;
    private float stateTimer;

    // 1 is right, -1 is left
    private float walkingSpeed;

    private float walkSoundTimer;

    GoonMoveComponent(Goon goon){
        this.goon = goon;

        init();
    }

    public void init(){
        currentState = State.WALK;
        stateTimer = Constants.GOON_WAIT_TIME;
        walkingSpeed = Constants.GOON_MOVE_SPEED;
        facingRight = true;

        walkSoundTimer = 0;
    }

    public void update(float delta){

        if (goon.getParentPlatform() == null) return;
        stateTimer -= delta;

        Body body = goon.getBody();

        //Walking
        if(currentState == State.WALK) {

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

            if (stateTimer <= 0) {
                currentState = State.WAIT;
                stateTimer = Constants.GOON_WAIT_TIME;
            }

            if(newX != 0) {
                walkSoundTimer -= delta;
                if (walkSoundTimer < 0) {
                    walkSoundTimer = Constants.GOON_WALK_SOUND_TIME;
                    int s = MathUtils.floor(MathUtils.random(1, 3.99f));
                    if (s == 1) {
                        DareSounds.STEPLOW1.play();
                    } else if (s == 2) {
                        DareSounds.STEPLOW2.play();
                    } else if (s == 3) {
                        DareSounds.STEPLOW3.play();
                    }
                }
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
