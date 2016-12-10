package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.TimeUtils;
import com.greenbatgames.ludumdare37.DareGame;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.screen.RestartScreen;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

// Laser grids turn on and off periodically, and if on, will kill player on contact
public class LaserGrid extends PhysicsBody implements Threat {

    private float onPeriod, offPeriod, timeUntilSwitch;
    private boolean active;

    public LaserGrid(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);

        onPeriod = 2f;
        offPeriod = 3f;
        timeUntilSwitch = onPeriod;
        active = true;
    }

    @Override
    public void act(float delta) {
        timeUntilSwitch -= delta;

        if (timeUntilSwitch < 0f)
            trigger();
    }

    // TODO: Override the draw method to draw lasers when active

    @Override
    protected void initPhysics(World world) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
                (getX() + getWidth() / 2.0f) / Constants.PTM,
                (getY() + getHeight() / 2.0f) / Constants.PTM
        );
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        // Rectangle body for mass (2x3 units, offset 1.5 units up)
        {
            PolygonShape shape = new PolygonShape();

            shape.setAsBox(
                    (getWidth() / 2.0f) / Constants.PTM,
                    (getHeight() / 2.0f) / Constants.PTM
            );

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.isSensor = true;

            body.createFixture(fixtureDef);
            shape.dispose();
        }

        body.setUserData(this);
    }

    @Override
    public void init() {

    }

    @Override
    public void touchPlayer(Player player) {
        if (active)
            GameScreen.level().killPlayer();
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
