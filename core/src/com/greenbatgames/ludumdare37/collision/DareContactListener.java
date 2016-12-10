package com.greenbatgames.ludumdare37.collision;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.greenbatgames.ludumdare37.entity.PhysicsBody;
import com.greenbatgames.ludumdare37.iface.Threat;
import com.greenbatgames.ludumdare37.player.Player;

/**
 * Created by Quiv on 10-12-2016.
 */

// TODO: All contacts and collisions go here
public class DareContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Object a = contact.getFixtureA().getBody().getUserData();
        Object b = contact.getFixtureB().getBody().getUserData();

        //DEBUG
        Gdx.app.log("","Colliding");

        if(!(a instanceof PhysicsBody) || !(b instanceof  PhysicsBody)){
            return;
        }

        if(a instanceof Player){
            handlePlayerContact((Player) a, (PhysicsBody) b);
        } else if (b instanceof Player) {
            handlePlayerContact((Player) b, (PhysicsBody) a);
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

    private void handlePlayerContact(Player player, PhysicsBody body){
        if(body instanceof Threat){
            ((Threat) body).touchPlayer(player);
        }
    }
}
