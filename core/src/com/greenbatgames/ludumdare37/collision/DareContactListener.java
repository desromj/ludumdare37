package com.greenbatgames.ludumdare37.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;

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

        // Ignore if we aren't working with two physics bodies
        if(!(a instanceof PhysicsBody) || !(b instanceof  PhysicsBody)){
            return;
        }

        // Player-specific collision
        if (a instanceof Player || b instanceof Player) {

            Player player;
            PhysicsBody other;
            Fixture fixturePlayer;

            if (a instanceof Player) {
                player = (Player) a;
                other = (PhysicsBody) b;
                fixturePlayer = contact.getFixtureA();
            } else {
                player = (Player) b;
                other = (PhysicsBody) a;
                fixturePlayer = contact.getFixtureB();
            }

            // Handle object interactions with player
            handlePlayerContact(player, other);

            // Handle player landing on physics bodies
            if (fixturePlayer == player.getFixture(Player.Fixtures.GROUND_SENSOR)) {
                Gdx.app.log(TAG, "increment foot contacts");
                player.mover().incNumFootContacts();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        if (a instanceof Player || b instanceof Player) {

            // Make it so we cannot jump indefinitely
            Player player = (Player) ((a instanceof Player) ? a : b);
            Fixture playerFixture = (a instanceof Player) ? contact.getFixtureA() : contact.getFixtureB();

            if (playerFixture == player.getFixture(Player.Fixtures.GROUND_SENSOR)) {
                Gdx.app.log(TAG, "decrement foot contacts");
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

    private void handlePlayerContact(Player player, PhysicsBody body){
        if(body instanceof Threat){
            ((Threat) body).touchPlayer(player);
        }
    }
}
