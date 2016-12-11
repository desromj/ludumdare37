package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Gdx;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by arne on 11-12-2016.
 */

public class PressurePlateComponent implements Initializable{
    PressurePlate pressurePlate;

    boolean pressed;
    boolean exploded;
    boolean explosionDone;

    boolean playerInRange;

    float pressedTimer;
    float explodedTimer;

    public PressurePlateComponent(PressurePlate pressurePlate){
        this.pressurePlate = pressurePlate;
    }

    public void init(){
        pressed = false;
        exploded = false;

        pressedTimer = Constants.PRESSURE_PLATE_DURATION;
    }

    public void pressDown(){
        pressed = true;
    }

    public void update(float delta){
        if(pressed && !exploded){
            pressedTimer -= delta;
            if(pressedTimer <= 0){
                explode();
            }
        }
        if(exploded){
            explodedTimer -= delta;
            if(explodedTimer <= 0){
                explosionDone = true;
            }
        }
    }

    public void explode(){
        exploded = true;
        explodedTimer = Constants.PRESSURE_PLATE_EXPLOSION_DURATION;
        if(playerInRange){
            pressurePlate.touchPlayer(GameScreen.level().getPlayer());
        }
    }
}
