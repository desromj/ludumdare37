package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.entity.DareLight;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;

import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * Created by Quiv on 10-12-2016.
 */

// Turrets patrol back and forth with a sensor cone, looking for the player
public class Turret extends PhysicsBody implements Threat {

    TurretAimComponent aimer;
    private float range;
    private float fov;

    PointLight glow;

    Sprite base, gunInactive, gunActive;
    Vector2 jointLocation;

    public Turret(float x, float y, float width, float height, float range, float fov, World world, RayHandler rayHandler) {
        super(x, y, width, height, world);

        jointLocation = new Vector2(
                x + width / 2f,
                y + height
        );

        aimer = new TurretAimComponent(this);
        aimer.setRange(range);
        this.range = range;
        aimer.setFOV(fov);
        this.fov = fov;

        glow = new PointLight(
                rayHandler,
                20,
                new Color(0f, 0.0f, 0.0f, 0.5f),
                50/Constants.PTM,
                jointLocation.x/Constants.PTM,
                jointLocation.y/Constants.PTM);
        glow.setActive(false);
        glow.setXray(true);
        glow.setContactFilter(DareLight.getFilter());

        base = new Sprite(new Texture(Gdx.files.internal("graphics/turretMount.png")));
        gunInactive = new Sprite(new Texture(Gdx.files.internal("graphics/turretInactive.png")));
        gunActive = new Sprite(new Texture(Gdx.files.internal("graphics/turretActive.png")));
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

            float r = Constants.TURRET_RANGE/Constants.PTM;
            float a = Constants.TURRET_ANG_RADIUS*MathUtils.degRad;

            shape.set(new float[]{
                    0, 0,
                    r*MathUtils.cos(a), r*MathUtils.sin(a),
                    r, 0,
                    r*MathUtils.cos(-a), r*MathUtils.sin(-a)
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
        aimer.init();
    }

    @Override
    public void touchPlayer(Player player) {
        GameScreen.level().killPlayer();
    }

    @Override
    public void act(float delta){
        super.act(delta);

        aimer.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        // Draw the mount
        batch.draw(
                base.getTexture(),
                getX(),
                getY()-getHeight()/2f,
                getX(),
                getY(),
                getWidth(),
                getHeight(),
                1f,
                1f,
                0f,
                0,
                0,
                base.getRegionWidth(),
                base.getRegionHeight(),
                false,
                false
        );

        // Draw inactive gun

        // Values are hardcoded, sorry :c Couldn't figure out how to get around issues with large sprite scaling
        batch.draw(
                gunInactive.getTexture(),
                getX() - Constants.TILE_WIDTH * 1.30f,
                getY() + Constants.TILE_WIDTH * 0.65f,
                Constants.TILE_WIDTH * 2.3f,
                Constants.TILE_WIDTH * 0.4f,
                getWidth() * (gunInactive.getWidth() / gunInactive.getHeight()) * 0.4f,
                getHeight() * 0.4f,
                1f,
                1f,
                MathUtils.radiansToDegrees * aimer.getCurrentAngle() + 180f,
                0,
                0,
                gunInactive.getRegionWidth(),
                gunInactive.getRegionHeight(),
                false,
                false
        );

        // Draw active gun, blended depending on how close it is to firing
        float activeRatio = aimer.getPercentCharged();

        if (activeRatio > 0) {
            glow.setActive(true);
            glow.setColor(1, 0.3f, 0.3f, MathUtils.clamp(activeRatio*0.8f, 0, 0.8f));
            glow.setPosition(
                    (jointLocation.x + 25*MathUtils.cos(aimer.getCurrentAngle()))/Constants.PTM,
                    (jointLocation.y + 25*MathUtils.sin(aimer.getCurrentAngle()))/Constants.PTM);
            batch.setColor(1, 1, 1, activeRatio);
            batch.draw(
                    gunActive.getTexture(),
                    getX() - Constants.TILE_WIDTH * 1.30f,
                    getY() + Constants.TILE_WIDTH * 0.65f,
                    Constants.TILE_WIDTH * 2.3f,
                    Constants.TILE_WIDTH * 0.4f,
                    getWidth() * (gunActive.getWidth() / gunActive.getHeight()) * 0.4f,
                    getHeight() * 0.4f,
                    1f,
                    1f,
                    MathUtils.radiansToDegrees * aimer.getCurrentAngle() + 180f,
                    0,
                    0,
                    gunActive.getRegionWidth(),
                    gunActive.getRegionHeight(),
                    false,
                    false
            );
        } else {
            glow.setActive(false);
        }

        // Reset colour to no tint on finish
        batch.setColor(1, 1, 1, 1);
    }

    /*
    * Getters and Setters
    */

    public void setPlayerInRange(boolean inRange){
        aimer.playerInRange = inRange;
    }

    public void setAim(float angle){
        Body b = getBody();
        getBody().setTransform(b.getPosition().x, b.getPosition().y, angle);
    }

    public TurretAimComponent getAimer(){
        return aimer;
    }
}
