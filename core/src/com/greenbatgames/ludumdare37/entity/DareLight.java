package com.greenbatgames.ludumdare37.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Filter;
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
                20,
                new Color(1, 1, 1, 0.8f),
                400/Constants.PTM,
                x/Constants.PTM,
                y/Constants.PTM,
                -90f,
                55f);
        init();
    }

    public void init(){
        light.setContactFilter(getFilter());
    }

    public static Filter getFilter(){
        Filter f = new Filter();
        f.categoryBits = 0x0001;
        f.maskBits = 0x0002;
        f.groupIndex = 0x0001;
        return f;
    }
}
