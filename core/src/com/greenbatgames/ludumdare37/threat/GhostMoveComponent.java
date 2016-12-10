package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.math.Vector2;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by arne on 10-12-2016.
 */

public class GhostMoveComponent implements Initializable {
    Ghost ghost;

    GhostMoveComponent(Ghost ghost){
        this.ghost = ghost;

        init();
    }

    public void init(){

    }

    public void update(Player player, float delta){
        Vector2 toPlayer = new Vector2(player.getX() - ghost.getX(), player.getY() - ghost.getY());
        toPlayer.setLength(Constants.GHOST_MOVE_SPEED);
        ghost.getBody().setLinearVelocity(
                toPlayer.x,
                toPlayer.y
        );
    }
}
