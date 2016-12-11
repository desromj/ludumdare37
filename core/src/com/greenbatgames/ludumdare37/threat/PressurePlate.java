package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 10-12-2016.
 */

// TODO: Pressure Plates explode after x seconds of stepping on them
public class PressurePlate extends PhysicsBody implements Threat {
    PressurePlateComponent presser;
    private Sprite sprite;

    boolean pressedDone;

    public PressurePlate(float x, float y, float width, float height, World world) {
        super(x, y, width, height, world);
        presser = new PressurePlateComponent(this);
        sprite = new Sprite(new Texture(Gdx.files.internal("graphics/pressurePlate.png")));
        pressedDone = false;

        init();
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

        {
            PolygonShape shape = new PolygonShape();


            float r = Constants.PRESSURE_PLATE_EXPLOSION_RADIUS/Constants.PTM;

            shape.set(new float[]{
                    r, 0,
                    r, r,
                    -r, r,
                    -r, 0
            });

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = Constants.PLAYER_DENSITY;
            fixtureDef.restitution = 0f;
            fixtureDef.isSensor = true;

            body.createFixture(fixtureDef);
            shape.dispose();
        }

        {
            PolygonShape shape = new PolygonShape();


            float r = getWidth()/2/Constants.PTM;
            float h = getHeight()/2/Constants.PTM;

            shape.set(new float[]{
                    r, 0,
                    r, h,
                    -r, h,
                    -r, 0
            });

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = Constants.PLAYER_DENSITY;
            fixtureDef.restitution = 0f;
            fixtureDef.isSensor = false;

            body.createFixture(fixtureDef);
            shape.dispose();
        }


        body.setUserData(this);
    }

    @Override
    public void init() {
        presser.init();
    }

    @Override
    public void touchPlayer(Player player) {
        if(!presser.explosionDone) {
            if (presser.exploded) {
                GameScreen.level().killPlayer();
            } else {
                presser.pressDown();
            }
        }
    }

    @Override
    public void act(float delta){
        presser.update(delta);
        if(presser.pressed && !pressedDone){
            setPosition(getX(), getY() - Constants.PRESSURE_PLATE_HEIGHT);
            //getBody().setTransform((getX() + getWidth()/2f)/Constants.PTM, (getY()-Constants.PRESSURE_PLATE_HEIGHT)/Constants.PTM, 0);
            pressedDone = true;
        }
    }

    public void draw(Batch batch, float parentAlpha) {

        if (pressedDone) return;

        batch.draw(
                sprite.getTexture(),
                getX(),
                getY() + getHeight()/2,
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

    public void setPlayerInRange(boolean inRange){
        presser.playerInRange = inRange;
    }
}
