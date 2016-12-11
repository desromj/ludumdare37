package com.greenbatgames.ludumdare37.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by arne on 11-12-2016.
 */

public enum DareSounds{
    STEP1("sounds/step1.mp3", 0.25f),
    STEP2("sounds/step2.mp3", 0.5f),
    STEP3("sounds/step3.mp3", 0.5f),
    STEP4("sounds/step4.mp3", 0.5f),
    STEPLOW1("sounds/stepLow1.mp3", 0.3f),
    STEPLOW2("sounds/stepLow2.mp3", 0.3f),
    STEPLOW3("sounds/stepLow3.mp3", 0.3f),
    BEEPHIGH("sounds/beepHigh.mp3", 0.5f),
    BEEPLOW("sounds/beepLow.mp3", 0.1f),
    CHARGE("sounds/turretCharge.mp3", 0.5f),
    DISCHARGE("sounds/turretDischarge.mp3", 0.5f),
    FIRE("sounds/turretFire.mp3", 1);

    Sound sound;
    float volume;


    private DareSounds(String filename, float v){
        sound = Gdx.audio.newSound(Gdx.files.internal(filename));
        volume = v;
    }

    public void play(){
        sound.play(volume);
    }

    public void play(float instVolume){
        sound.play(instVolume);
    }

    public void stop(){
        sound.stop();
    }

    /*
    Getters and setters
     */

    public void setVolume(float v){
        volume = MathUtils.clamp(v, 0, 1);
    }
}
