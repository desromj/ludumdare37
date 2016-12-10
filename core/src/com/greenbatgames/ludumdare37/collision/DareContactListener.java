package com.greenbatgames.ludumdare37.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.threat.Turret;

/**
 * Created by Quiv on 10-12-2016.
 */

// TODO: All contacts and collisions go here
public class DareContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(!(a instanceof PhysicsBody) || !(b instanceof  PhysicsBody)){
            return;
        }

        if(a instanceof Player && b instanceof Threat){
            //Check if there is physical contact
            if(!fixA.isSensor() && !fixB.isSensor()){
                ((Threat) b).touchPlayer((Player) a);
            } else if(!fixA.isSensor() && fixB.isSensor()){
                if(b instanceof Turret){

                }
            }
        } else if (b instanceof Player && a instanceof Threat) {
            if(!fixA.isSensor() && !fixB.isSensor()){
                ((Threat) a).touchPlayer((Player) b);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
