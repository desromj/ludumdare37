package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.greenbatgames.ludumdare37.DareGame;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.screen.RestartScreen;

/**
 * Created by Quiv on 10-12-2016.
 */

// Laser grids turn on and off periodically, and if on, will kill player on contact
public class LaserGrid extends PhysicsBody implements Threat {

    private float onPeriod, offPeriod, timeUntilSwitch;
    private long startTime;
    private boolean active;

    public LaserGrid(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);

        onPeriod = 2f;
        offPeriod = 3f;
        timeUntilSwitch = onPeriod;
        startTime = TimeUtils.nanoTime();
        active = true;
    }

    @Override
    public void act(float delta) {
        timeUntilSwitch -= delta;

        if (timeUntilSwitch < 0f)
            trigger();

        if (!active) return;
    }

    // TODO: Override the draw method to draw lasers when active

    @Override
    protected void initPhysics(World world) {

    }

    @Override
    public void init() {

    }

    @Override
    public void touchPlayer(Player player) {
        if (active)
            DareGame.setScreen(RestartScreen.class);
    }

    private void trigger() {
        if (active) {
            timeUntilSwitch = offPeriod;
        } else {
            timeUntilSwitch = onPeriod;
        }

        active = !active;
    }
}
