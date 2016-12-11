package com.greenbatgames.ludumdare37.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

public class Player extends PhysicsBody implements Initializable {

    public enum Fixtures {
        BASE,
        BODY,
        GROUND_SENSOR
    }

    private MoveComponent mover;
    private ClimbComponent climber;

    private boolean dead;
    private Sprite sprite;

    public Player(float x, float y, float width, float height, World world)
    {
        super(x, y, width, height, world);

        mover = new MoveComponent(this);
        climber = new ClimbComponent(this);
        sprite = new Sprite(new Texture(Gdx.files.internal("graphics/player.png")));

        init();
    }


    @Override
    public void init()
    {
        mover.init();
        climber.init();
        dead = false;
    }



    @Override
    protected void initPhysics(World world) {

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

        // Circle base for walking (one unit radius, centre point is centre of body)
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
            fixtureDef.friction = Constants.PLAYER_GROUND_FRICTION;
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
            fixtureDef.isSensor = true;

            body.createFixture(fixtureDef);
            shape.dispose();
        }

        // Set our new fixtures with user data of the specific fixture type they are
        assert body.getFixtureList().size == Fixtures.values().length;

        for (int i = 0; i < body.getFixtureList().size; i++) {
            body.getFixtureList().get(i).setUserData(Fixtures.values()[i]);
        }

        // End by setting the body user data to the Player
        body.setUserData(this);
    }



    @Override
    public void act(float delta) {

        if (dead) return;

        super.act(delta);

        // Run Component updates in sequence, and break through
        // any later updates if we receive a returned request to
        do {
            if (!climber.update(delta)) break;
            if (!mover.update(delta)) break;
        } while (false);

        // Ensure the player is always ready to respond to physics collisions
        body.setAwake(true);
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (dead) return;

        batch.draw(
                sprite.getTexture(),
                getX(),
                getY() + Constants.PLAYER_RADIUS,
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
                !mover.isFacingRight(),
                false
        );
    }



    /*
        Getters and Setters
     */

    public Fixture getFixture(Fixtures type) {
        for (Fixture fix: body.getFixtureList())
            if (fix.getUserData() == type)
                return fix;
        return null;
    }

    public boolean isCollisionDisabled() { return mover.isCollisionDisabled(); }

    public MoveComponent mover() { return mover; }
    public ClimbComponent climber() { return climber; }

    public void setDead(boolean value) {
        this.dead = value;
        body.setLinearVelocity(0f, 0f);
    }

    public boolean isDead() { return dead; }

    public boolean isJumpButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_JUMP);
    }
    public boolean isClimbButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_ATTACK);
    }
    public boolean isWalkButtonHeld() { return Gdx.input.isKeyPressed(Constants.KEY_RIGHT) || Gdx.input.isKeyPressed(Constants.KEY_LEFT); }
}
