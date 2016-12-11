package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

// TODO: Pressure Plates explode after x seconds of stepping on them
public class PressurePlate extends PhysicsBody implements Threat {
    PressurePlateComponent presser;

    public PressurePlate(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);
        presser = new PressurePlateComponent(this);

        init();
    }

    @Override
    protected void initPhysics(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(
                (getX() + getWidth() / 2.0f) / Constants.PTM,
                (getY() + getHeight() / 2.0f) / Constants.PTM
        );
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        {
            PolygonShape shape = new PolygonShape();


            float r = Constants.PRESSURE_PLATE_EXPLOSION_RADIUS/Constants.PTM;

            shape.set(new float[]{
                    r, 0,
                    r, r,
                    -r, r,
                    -r, 0
            });

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = Constants.PLAYER_DENSITY;
            fixtureDef.restitution = 0f;
            fixtureDef.isSensor = true;

            body.createFixture(fixtureDef);
            shape.dispose();
        }

        body.setUserData(this);
    }

    @Override
    public void init() {
        presser.init();
    }

    @Override
    public void touchPlayer(Player player) {

    }

    public void setPlayerInRange(boolean inRange){
        presser.playerInRange = inRange;
    }
}
