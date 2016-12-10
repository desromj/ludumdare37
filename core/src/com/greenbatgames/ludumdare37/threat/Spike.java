package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.DareGame;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.RestartScreen;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

// Spikes kill the player on touch
public class Spike extends PhysicsBody implements Threat {

    public Spike(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);
    }

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
        DareGame.setScreen(RestartScreen.class);
    }
}
