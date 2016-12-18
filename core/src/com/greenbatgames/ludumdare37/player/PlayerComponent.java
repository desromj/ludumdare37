package com.greenbatgames.ludumdare37.player;

import com.greenbatgames.ludumdare37.iface.Initializable;

/**
 * Created by Quiv on 10-12-2016.
 */

public abstract class PlayerComponent implements Initializable {

    protected Player player;

    public PlayerComponent(Player player) {
        this.player = player;
        init();
    }

    /**
     * Standard update method, altered to return a status flag
     *
     * @param delta delta time since last update
     * @return true if the update loop should keep going in the sequence of Component updates
     */
    public abstract boolean update(float delta);

    // Touch methods, for use in child classes
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }
}
