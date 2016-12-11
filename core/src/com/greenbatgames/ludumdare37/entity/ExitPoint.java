package com.greenbatgames.ludumdare37.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

public class ExitPoint extends PhysicsBody {

    boolean triggered;
    Sprite sprite;

    public ExitPoint(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);
        triggered = false;
        sprite = new Sprite(new Texture(Gdx.files.internal("graphics/doorInactive.png")));
    }

    @Override
    protected void initPhysics(World world) {

        // Utility unit = one player radius expressed in Box2D units
        float b2Unit = (getWidth() / 2.0f) / Constants.PTM;

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

            shape.set(new float[]{
                    b2Unit, 0f,
                    b2Unit, b2Unit * 3f,
                    -b2Unit, b2Unit * 3f,
                    -b2Unit, 0f
            });

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
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(
                sprite.getTexture(),
                getX(),
                getY() + getHeight() / 2f,
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                1f,
                1f,
                0f,
                0,
                0,
                sprite.getRegionWidth(),
                sprite.getRegionHeight(),
                false,
                false
        );
    }

    public boolean alreadyTriggered() { return this.triggered; }
    public void trigger() { this.triggered = true; }
}
