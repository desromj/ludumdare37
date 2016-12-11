package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
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

// TODO: Turrets patrol back and forth with a sensor cone, looking for the player
public class Turret extends PhysicsBody implements Threat {
    TurretAimComponent aimer;

    public Turret(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);

        aimer = new TurretAimComponent(this);
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

        // Utility unit = one player radius expressed in Box2D units
        float b2Unit = Constants.PLAYER_RADIUS / Constants.PTM;

        // Cone-shaped sensor representing the turret's field of view
        {
            PolygonShape shape = new PolygonShape();

            float r = Constants.TURRET_RANGE/Constants.PTM;
            float a = Constants.TURRET_ANG_RADIUS;
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

    /*
    * Getters and Setters
    */

    public void setPlayerInRange(boolean inRange){
        aimer.playerInRange = inRange;
        Gdx.app.log("", "In range: " + String.valueOf(inRange));
    }

    public void setAim(float angle){
        Body b = getBody();
        getBody().setTransform(b.getPosition().x, b.getPosition().y, angle);
    }
}
