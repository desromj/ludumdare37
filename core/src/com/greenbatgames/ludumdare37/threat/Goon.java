package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;

/**
 * Created by Quiv on 10-12-2016.
 */

// TODO: Goons move back and forth on the platform they are standing on, or until hitting a wall
public class Goon extends PhysicsBody implements Threat {

    public Goon(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);
    }

    @Override
    protected void initPhysics(World world) {

    }

    @Override
    public void init() {

    }

    @Override
    public void touchPlayer(Player player) {

    }
}
