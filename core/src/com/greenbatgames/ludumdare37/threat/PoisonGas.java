package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greenbatgames.ludumdare37.iface.Initializable;

/**
 * Created by Quiv on 10-12-2016.
 */

// TODO: Fill up the screen from the bottom (or top, depending)
public class PoisonGas extends Actor implements Initializable {

    private boolean fromTop;

    public PoisonGas(boolean fromTop) {
        this.fromTop = fromTop;
    }

    @Override
    public void init() {
        // TODO: Add the Timer to the game stage after initialization
    }

    // TODO: Update the timer counter here. When it is 0, just kill the player
}
