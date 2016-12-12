package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;
import com.greenbatgames.ludumdare37.util.DareSounds;

/**
 * Created by arne on 11-12-2016.
 */

public class PressurePlateComponent implements Initializable {
    PressurePlate pressurePlate;

    boolean pressed;
    boolean exploded;
    boolean explosionDone;

    boolean playerInRange;

    float pressedTimer;
    float explodedTimer;

    Sound pressurePlateSound;
    boolean soundPlaying;

    public PressurePlateComponent(PressurePlate pressurePlate){
        this.pressurePlate = pressurePlate;
    }

    public void init(){
        pressed = false;
        exploded = false;

        pressedTimer = Constants.PRESSURE_PLATE_DURATION;

        pressurePlateSound = DareSounds.PRESSUREPLATE.getSound();
        soundPlaying = false;
    }

    public void pressDown(){
        pressed = true;
    }

    public void update(float delta){
        if(pressed && !exploded){
            if(!soundPlaying){
                pressurePlateSound.play(DareSounds.PRESSUREPLATE.getVolume());
                soundPlaying = true;
            }
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
