package com.greenbatgames.ludumdare37.entity;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Quiv on 10-12-2016.
 */

// TODO: On contact with the player, exit the level and load the next
public class ExitPoint extends PhysicsBody {

    public ExitPoint(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);
    }

    @Override
    protected void initPhysics(World world) {

    }

    @Override
    public void init() {

    }
}
