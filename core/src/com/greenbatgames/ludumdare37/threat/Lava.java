package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

/**
 * Created by Quiv on 10-12-2016.
 */

// Lava BURNS THE PLAYER TO DEATH
public class Lava extends PhysicsBody implements Threat {

    Sprite sprite;

    ConeLight light;

    ParticleEffect peLeft, peRight;

    public Lava(float x, float y, float width, float height, World world, RayHandler rayHandler) {
        super(x, y, width, height, world);
        sprite = new Sprite(new Texture(Gdx.files.internal("tiles/lava.png")));
        sprite.getTexture().setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

        light = new ConeLight(
                rayHandler,
                20,
                new Color(0.5f, 0.25f, 0f, 1f),
                width/Constants.PTM,
                (x+width/2)/Constants.PTM,
                (y+1)/Constants.PTM,
                90f,
                90f);

        // Load particle effects
        peLeft = new ParticleEffect();
        peLeft.load(
                Gdx.files.internal("graphics/lava-bubble.pe"),
                Gdx.files.internal("graphics"));
        peLeft.setPosition(getX(), getY());
        peLeft.scaleEffect(0.25f);
        peLeft.start();

        peRight = new ParticleEffect();
        peRight.load(
                Gdx.files.internal("graphics/lava-bubble.pe"),
                Gdx.files.internal("graphics"));
        peRight.setPosition(getX() + getWidth(), getY());
        peRight.scaleEffect(0.25f);
        peRight.start();
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
        GameScreen.level().killPlayer();
    }

    @Override
    public void act(float delta) {
        peLeft.update(delta);
        peRight.update(delta);

        if (peLeft.isComplete()) peLeft.reset();
        if (peRight.isComplete()) peRight.reset();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(
                sprite.getTexture(),
                getX(),
                getY(),
                getWidth(),
                getHeight()
        );

        peLeft.draw(batch);
        peRight.draw(batch);
    }
}
