package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;

/**
 * Created by Quiv on 10-12-2016.
 */

// TODO: Turrets patrol back and forth with a sensor cone, looking for the player
public class Turret extends PhysicsBody implements Threat {

    public Turret(float x, float y, float width, float height, World world) {
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
