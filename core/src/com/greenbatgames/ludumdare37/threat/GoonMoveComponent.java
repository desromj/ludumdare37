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

    //TODO: Put this in an enum instead
    private int currentState;
    private float stateTimer;

    // 1 is right, -1 is left
    private float walkingSpeed;

    GoonMoveComponent(Goon goon){
        this.goon = goon;

        init();
    }

    public void init(){
        currentState = 0;
        stateTimer = Constants.GOON_WAIT_TIME;
        walkingSpeed = Constants.GOON_MOVE_SPEED;
    }

    public void update(float delta){
        stateTimer -= delta;

        Body body = goon.getBody();
        //Walking
        if(currentState == 0){
            body.setLinearVelocity(
                    walkingSpeed,
                    body.getLinearVelocity().y);
            if(stateTimer <= 0){
                currentState = 1;
                stateTimer = Constants.GOON_WAIT_TIME;
            }

        //Standing still
        } else if(currentState == 1){
            body.setLinearVelocity(
                    0f,
                    body.getLinearVelocity().y);
            if(stateTimer <= 0){
                currentState = 0;
                walkingSpeed = -walkingSpeed;
                stateTimer = Constants.GOON_WALK_TIME;
            }
        }

    }

}
