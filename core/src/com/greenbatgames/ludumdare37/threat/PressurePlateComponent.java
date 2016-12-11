package com.greenbatgames.ludumdare37.threat;

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

    boolean playerInRange;

    float pressedTimer;

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

    public void act(float delta){
        if(pressed && !exploded){
            pressedTimer -= delta;
            if(pressedTimer <= 0){
                explode();
            }
        }
    }

    public void explode(){
        exploded = true;
        if(playerInRange){
            pressurePlate.touchPlayer(GameScreen.level().getPlayer());
        }
    }
}
