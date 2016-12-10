package com.greenbatgames.ludumdare37.threat;

import com.greenbatgames.ludumdare37.iface.Initializable;

/**
 * Created by arne on 10-12-2016.
 */

public class GoonMoveComponent implements Initializable {
    //TODO: Put this in an enum instead
    private int currentState;
    float stateTimer;

    GoonMoveComponent(){
        init();
    }

    @Override
    public void init(){
        currentState = 0;
    }

    public void update(float delta){
        stateTimer -= delta;

        //Walking
        if(currentState == 0){


        //Standing still
        } else if(currentState == 1){

        }

    }

}
