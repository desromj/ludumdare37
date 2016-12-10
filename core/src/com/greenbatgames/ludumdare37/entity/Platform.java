package com.greenbatgames.ludumdare37.entity;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

public class Platform extends PhysicsBody {

    public Platform(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);
    }

    @Override
    public void init() {

    }

    @Override
    protected void initPhysics(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
                (getX() + getWidth() / 2.0f) / Constants.PTM,
                (getY() + getHeight() / 2.0f) / Constants.PTM);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                (getWidth() / 2.0f) / Constants.PTM,
                (getHeight() / 2.0f) / Constants.PTM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        this.body = world.createBody(bodyDef);
        this.body.createFixture(fixtureDef);
        this.body.setUserData(this);

        shape.dispose();
    }
}
