package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;
import com.greenbatgames.ludumdare37.util.DareSounds;

/**
 * Created by arne on 10-12-2016.
 */

public class GhostMoveComponent implements Initializable {

    Ghost ghost;
    boolean facingRight;

    private Sound soundHover;
    private Sound soundHaunt;
    long hauntSoundID;

    boolean soundPlaying;
    boolean soundShouldBePlaying;

    GhostMoveComponent(Ghost ghost){
        this.ghost = ghost;

        init();
    }

    public void init(){
        facingRight = true;
        soundPlaying = false;
        soundShouldBePlaying = true;

        soundHover = DareSounds.GHOSTHOVER.getSound();
        soundHaunt = DareSounds.GHOSTHAUNT.getSound();
    }

    public void update(Player player, float delta){
        if(!soundPlaying && soundShouldBePlaying) {
            soundHover.loop(DareSounds.GHOSTHOVER.getVolume());
            hauntSoundID = soundHaunt.loop(0);
            soundPlaying = true;
        }

        Vector2 toPlayer = new Vector2(player.getX() - ghost.getX(), player.getY() - ghost.getY());
        float distance = toPlayer.len();

        toPlayer.setLength(Constants.GHOST_MOVE_SPEED);
        ghost.getBody().setLinearVelocity(
                toPlayer.x,
                toPlayer.y
        );

        soundHaunt.setVolume(hauntSoundID, MathUtils.clamp(1 - distance*0.004f + 0.1f, 0, 1));

        facingRight = ghost.getBody().getLinearVelocity().x >= 0;

        if(GameScreen.level().getPlayer().isDead()){

        }
    }

    public boolean isFacingRight() { return facingRight; }

    public void stopPlayingSound(){
        soundShouldBePlaying = false;
        soundHover.stop();
        soundHaunt.stop();
    }
}
