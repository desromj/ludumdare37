package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
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

public class Ghost extends PhysicsBody implements Threat {
    GhostMoveComponent mover;

    public Ghost(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);
        mover = new GhostMoveComponent(this);
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

        // Circle base for collisions (one unit radius, centre point is centre of body)
        {
            CircleShape shape = new CircleShape();
            shape.setRadius(b2Unit);
            shape.setPosition(new Vector2(0, 0));

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = Constants.PLAYER_DENSITY;
            fixtureDef.restitution = 0f;
            fixtureDef.friction = Constants.PLAYER_GROUND_FRICTION;
            fixtureDef.isSensor = false;

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

    }

    @Override
    public void act(float delta){
        super.act(delta);

        mover.update(Player.getInstance(), delta);
    }
}