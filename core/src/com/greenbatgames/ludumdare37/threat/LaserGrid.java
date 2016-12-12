package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.entity.DareLight;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;
import com.greenbatgames.ludumdare37.util.DareSounds;

import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * Created by Quiv on 10-12-2016.
 */

// Laser grids turn on and off periodically, and if on, will kill player on contact
public class LaserGrid extends PhysicsBody implements Threat {

    private float onPeriod, offPeriod, timeUntilSwitch, warmupTime;
    private boolean active;

    PointLight light;

    private Sprite bottom, top, laser;

    Sound laserSound;
    boolean soundPlaying;

    public LaserGrid(float x, float y, float width, float height, World world, RayHandler rayHandler) {
        super(x, y, width, height, world);

        onPeriod = 2f;
        offPeriod = 3f;
        warmupTime = 0.5f;
        timeUntilSwitch = onPeriod;
        active = true;

        light = new PointLight(
                rayHandler,
                20,
                new Color(1f, 0.3f, 0.3f, 0.5f),
                Math.max(height, width)/ Constants.PTM,
                (x + width/2)/ Constants.PTM,
                (y + height/2)/Constants.PTM);
        light.setContactFilter(DareLight.getFilter());

        bottom = new Sprite(new Texture(Gdx.files.internal("graphics/laserGridBase.png")));
        top = new Sprite(new Texture(Gdx.files.internal("graphics/laserGridTop.png")));
        laser = new Sprite(new Texture(Gdx.files.internal("graphics/laserGrid.png")));

        laserSound = DareSounds.LASERGRIDFIRE.getSound();
        soundPlaying = false;
    }

    @Override
    public void act(float delta) {
        timeUntilSwitch -= delta;

        if (timeUntilSwitch < 0f)
            trigger();
        if(active && !soundPlaying){
            laserSound.play(DareSounds.LASERGRIDFIRE.getVolume());
            soundPlaying = true;
        } else if(!active && soundPlaying){
            laserSound.stop();
            soundPlaying = false;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // Draw the lasers if active. If not active, draw their alpha based on percentage of warmup
        // Use circleOut interpolation with this percentage
        float ratio;

        light.setActive(active);
        if (active) {
            ratio = 1.0f;
        } else if (timeUntilSwitch > warmupTime) {
            ratio = 0.0f;
        } else {
            ratio = (warmupTime - timeUntilSwitch) / warmupTime;
            ratio = Interpolation.circleOut.apply(ratio)*0.3f;
        }

        // Draw lasers if the ratio is above 0
        if (ratio > 0f) {
            batch.setColor(1, 1, 1, ratio);
            batch.draw(
                    laser,
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight()
            );
            batch.setColor(1, 1, 1, 1);
        }

        // Then draw the bottom and top over the lasers, always
        batch.draw(
                bottom,
                getX(),
                getY(),
                getWidth(),
                getHeight()
        );

        batch.draw(
                top,
                getX(),
                getY(),
                getWidth(),
                getHeight()
        );
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

        // Check for player collision if we immediately trigger
        if (active) {

            // Check if the player is within range, if active
            if (active) {
                GameScreen.level().getWorld().QueryAABB(
                        new QueryCallback() {
                            @Override
                            public boolean reportFixture(Fixture fixture) {

                                Player player = GameScreen.level().getPlayer();
                                if (fixture.getBody().getUserData() == player && !player.isDead()) {
                                    touchPlayer(player);
                                }
                                return true;
                            }
                        },
                        (getLeft() - getWidth() / 4f) / Constants.PTM,
                        (getBottom() - getHeight() / 4f) / Constants.PTM,
                        (getRight() + getWidth() / 4f) / Constants.PTM,
                        (getTop() + getHeight() / 4f) / Constants.PTM);
            }
        }
    }

    /*
    Getters and setters
     */

    public float getOnPeriod(){
        return onPeriod;
    }

    public void setOnPeriod(float t){
        onPeriod = t;
    }

    public void setOffPeriod(float t){
        offPeriod = t;
    }

    public void setWarmupTime(float t){
        warmupTime = t;
    }

    public void setTimeUntilSwitch(float t){
        timeUntilSwitch = t;
    }
}
