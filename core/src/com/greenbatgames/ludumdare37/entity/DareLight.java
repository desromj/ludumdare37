package com.greenbatgames.ludumdare37.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.util.Constants;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

/**
 * Created by arne on 12-12-2016.
 */

public class DareLight extends Actor implements Initializable{
    ConeLight light;

    boolean flickering;

    public DareLight(float x, float y, RayHandler rayHandler, World world){
        light = new ConeLight(
                rayHandler,
                50,
                new Color(1, 1, 1, 0.8f),
                400/Constants.PTM,
                x/Constants.PTM,
                y/Constants.PTM,
                -90f,
                55f);
        init();
    }

    public void init(){

    }
}
