package com.greenbatgames.ludumdare37.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.greenbatgames.ludumdare37.entity.ExitPoint;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.entity.Platform;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.threat.Goon;
import com.greenbatgames.ludumdare37.threat.PressurePlate;
import com.greenbatgames.ludumdare37.threat.Turret;

/**
 * Created by Quiv on 10-12-2016.
 */

// TODO: All contacts and collisions go here
public class DareContactListener implements ContactListener {

    public static final String TAG = DareContactListener.class.getSimpleName();

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(!(a instanceof PhysicsBody) || !(b instanceof  PhysicsBody)){
            return;
        }

        // Initialize parent Platforms for Goons
        if ((a instanceof Goon && b instanceof Platform)
            || (b instanceof Goon && a instanceof Platform)) {

            Goon goon = (Goon) ((a instanceof Goon) ? a : b);
            Platform platform = (Platform) ((a instanceof Goon) ? b : a);

            if (!goon.hasParentPlatform())
                goon.setParentPlatform(platform);
        }

        // Handle Player/Threat interactions
        if(a instanceof Player && b instanceof Threat){
            //Check if there is physical contact
            if(!fixA.isSensor() && !fixB.isSensor()){
                ((Threat) b).touchPlayer((Player) a);
            //Check if any sensor fixtures have found the player
            } else if(!fixA.isSensor() && fixB.isSensor()){
                if(b instanceof Turret) {
                    ((Turret) b).setPlayerInRange(true);
                } else if(b instanceof PressurePlate) {
                    ((PressurePlate) b).setPlayerInRange(true);
                } else {
                    ((Threat) b).touchPlayer((Player) a);   // Default to sensors touching the player too
                }
            }
        } else if (b instanceof Player && a instanceof Threat) {
            if (!fixA.isSensor() && !fixB.isSensor()) {
                ((Threat) a).touchPlayer((Player) b);
            } else if(fixA.isSensor() && !fixB.isSensor()){
                if(a instanceof Turret){
                    ((Turret) a).setPlayerInRange(true);
                } else if(a instanceof PressurePlate) {
                    ((PressurePlate) a).setPlayerInRange(true);
                } else {
                    ((Threat) a).touchPlayer((Player) b);   // Default to sensors touching the player too
                }
           }
        }

        // Player-specific collision
        if (a instanceof Player || b instanceof Player) {

            Player player;
            PhysicsBody other;
            Fixture fixturePlayer, fixtureOther;

            if (a instanceof Player) {
                player = (Player) a;
                other = (PhysicsBody) b;
                fixturePlayer = contact.getFixtureA();
                fixtureOther = contact.getFixtureB();
            } else {
                player = (Player) b;
                other = (PhysicsBody) a;
                fixturePlayer = contact.getFixtureB();
                fixtureOther = contact.getFixtureA();
            }

            // Handle player landing on physics bodies
            if (!(fixtureOther.isSensor())
                    && fixturePlayer == player.getFixture(Player.Fixtures.GROUND_SENSOR)) {
                player.mover().incNumFootContacts();
            }

            // Player colliding with the exit point - load next level
            if (other instanceof ExitPoint) {
                ExitPoint point = (ExitPoint) other;

                if (!point.alreadyTriggered()) {
                    point.trigger();
                    GameScreen.getInstance().nextLevel();
                    Gdx.app.log(TAG, "Exit point triggered");
                }
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(a instanceof Player && b instanceof Threat){
            if(!fixA.isSensor() && fixB.isSensor()){
                if(b instanceof Turret){
                    ((Turret) b).setPlayerInRange(false);
                } else if(b instanceof PressurePlate) {
                    ((PressurePlate) b).setPlayerInRange(true);
                }
            }
        } else if (b instanceof Player && a instanceof Threat) {
            if(fixA.isSensor() && !fixB.isSensor()){
                if(a instanceof Turret){
                    ((Turret) a).setPlayerInRange(false);
                } else if(a instanceof PressurePlate) {
                    ((PressurePlate) a).setPlayerInRange(true);
                }
            }
        }

        if(!(a instanceof PhysicsBody) || !(b instanceof  PhysicsBody)){
            return;
        }

        //User-specific collision
        if (a instanceof Player || b instanceof Player) {

            // Make it so we cannot jump indefinitely
            Player player = (Player) ((a instanceof Player) ? a : b);
            Fixture playerFixture = (a instanceof Player) ? contact.getFixtureA() : contact.getFixtureB();
            Fixture otherFixture = (a instanceof Player) ? contact.getFixtureB() : contact.getFixtureA();

            if (!(otherFixture.isSensor())
                    && playerFixture == player.getFixture(Player.Fixtures.GROUND_SENSOR)) {
                player.mover().decNumFootContacts();
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
