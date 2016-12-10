package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
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

// TODO: Goons move back and forth on the platform they are standing on, or until hitting a wall
public class Goon extends PhysicsBody implements Threat {

    private GoonMoveComponent mover;

    public Goon(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);

        mover = new GoonMoveComponent(this);

        init();
    }

    @Override
    protected void initPhysics(World world) {
        //TODO: Copied over from Player class, should be altered.
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
                (getX() + getWidth() / 2.0f) / Constants.PTM,
                (getY() + getHeight() / 2.0f) / Constants.PTM
        );
        bodyDef.fixedRotation = true;

        body = world.createBody(bodyDef);

        // Utility unit = one player radius expressed in Box2D units
        float b2Unit = Constants.PLAYER_RADIUS / Constants.PTM;

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
            fixtureDef.density = Constants.PLAYER_DENSITY;
            fixtureDef.restitution = 0f;
            fixtureDef.friction = 1f;
            fixtureDef.isSensor = false;

            body.createFixture(fixtureDef);
            shape.dispose();
        }

        // Square sensor for floor contact (1x1, offset 1 unit down)
        {
            PolygonShape shape = new PolygonShape();

            shape.set(new float[]{
                    b2Unit * 0.5f, -b2Unit * 1.5f,
                    b2Unit * 0.5f, -b2Unit * 0.5f,
                    -b2Unit * 0.5f, -b2Unit * 0.5f,
                    -b2Unit * 0.5f, -b2Unit * 1.5f,
            });

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = Constants.PLAYER_DENSITY;
            fixtureDef.restitution = 0f;
            fixtureDef.friction = 1f;
            fixtureDef.isSensor = true;

            body.createFixture(fixtureDef);
            shape.dispose();
        }

        // Set our new fixtures with user data of the specific fixture type they are
        assert body.getFixtureList().size == Player.Fixtures.values().length;

        for (int i = 0; i < body.getFixtureList().size; i++) {
            body.getFixtureList().get(i).setUserData(Player.Fixtures.values()[i]);
        }

        // End by setting the body user data to the Player
        body.setUserData(this);
    }

    @Override
    public void init() {
        mover.init();
    }

    @Override
    public void touchPlayer(Player player) {
        DareGame.setScreen(RestartScreen.class);
    }

    @Override
    public void act(float delta){
        super.act(delta);

        mover.update(delta);
    }
}
