package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.util.Constants;

import box2dLight.RayHandler;

/**
 * Created by arne on 12-12-2016.
 */

//TODO: FUTURE WORK: make this work.
public class TurretSniper extends Turret {

    public TurretSniper(float x, float y, float width, float height, World world, RayHandler rayHandler){
        super(x, y, width, height, world, rayHandler);
    }

    @Override
    protected void initPhysics(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(
                (getX() + getWidth() / 2.0f) / Constants.PTM,
                (getY() + getHeight()) / Constants.PTM
        );
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        // Cone-shaped sensor representing the turret's field of view
        {
            PolygonShape shape = new PolygonShape();

            float r = Constants.TURRET_RANGE / Constants.PTM * 10f;
            float a = Constants.TURRET_ANG_RADIUS * MathUtils.degRad / 5;

            shape.set(new float[]{
                    0, 0,
                    r * MathUtils.cos(a), r * MathUtils.sin(a),
                    r, 0,
                    r * MathUtils.cos(-a), r * MathUtils.sin(-a)
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
}
