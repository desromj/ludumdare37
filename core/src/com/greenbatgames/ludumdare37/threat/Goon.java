package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.entity.Platform;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

// Goons move back and forth on the platform they are standing on, or until hitting a wall
public class Goon extends PhysicsBody implements Threat {

    private Platform parent = null;
    private GoonMoveComponent mover;
    private Sprite sprite;

    public Goon(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);

        mover = new GoonMoveComponent(this);
        sprite = new Sprite(new Texture(Gdx.files.internal("graphics/goon.png")));

        init();
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

        // Rectangle body for mass (2x4 units, offset 2 units up)
        {
            PolygonShape shape = new PolygonShape();

            shape.set(new float[]{
                    b2Unit, 0f,
                    b2Unit, b2Unit * 4f,
                    -b2Unit, b2Unit * 4f,
                    -b2Unit, 0f
            });

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = Constants.PLAYER_DENSITY;
            fixtureDef.restitution = 0f;
            fixtureDef.friction = Constants.PLAYER_GROUND_FRICTION;
            fixtureDef.isSensor = false;
            fixtureDef.filter.categoryBits = 0x0004;
            fixtureDef.filter.maskBits = (short) 0xFFFB;

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

        // End by setting the body user data to the Player
        body.setUserData(this);
    }

    @Override
    public void init() {
        mover.init();
    }

    @Override
    public void touchPlayer(Player player) {
        GameScreen.level().killPlayer();
    }

    @Override
    public void act(float delta){
        super.act(delta);

        mover.update(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(
                sprite.getTexture(),
                getX(),
                getY() + Constants.PLAYER_RADIUS * 2f,
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

    public boolean hasParentPlatform() {
        return parent != null;
    }

    public void setParentPlatform(Platform parent) {
        this.parent = parent;
    }

    public Platform getParentPlatform() { return parent; }
}
