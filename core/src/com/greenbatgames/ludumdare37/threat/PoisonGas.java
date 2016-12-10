package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greenbatgames.ludumdare37.DareGame;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.screen.RestartScreen;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

public class PoisonGas extends Actor implements Initializable {

    //initial duration will be used for drawing the rising cloud of gas
    private float initialDuration;
    private float duration;
    private boolean fromTop;

    public PoisonGas(boolean fromTop) {
        this.fromTop = fromTop;
        init();
    }

    @Override
    public void init() {
        initialDuration = Constants.GAS_DURATION;
        duration = initialDuration;
    }

    @Override
    public void act(float delta){
        duration -= delta;
        if(duration <= 0){
            DareGame.setScreen(RestartScreen.class);
        }
    }
}
