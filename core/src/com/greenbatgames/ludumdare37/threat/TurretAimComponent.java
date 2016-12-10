package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.math.Vector2;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by arne on 10-12-2016.
 */

public class TurretAimComponent implements Initializable {
    Turret turret;

    boolean playerInRange;
    boolean playerInSight;

    float waitTimer;
    float currentAngle;
    float rotationSpeed;

    float aimTimer;

    int state;

    public TurretAimComponent(Turret turret){
        this.turret = turret;
        init();
    }

    public void init(){
        playerInRange = false;
        playerInSight = false;

        waitTimer = 0;
        aimTimer = 0;
        currentAngle = 0;
        rotationSpeed = Constants.TURRET_ROTATION_SPEED;
        state = 0;
    }

    public void update(float delta){
        waitTimer -= delta;
        checkPlayerInSight();

        Player player = GameScreen.level().getPlayer();
        if(playerInSight){
            Vector2 toPlayer = new Vector2(player.getX() - turret.getX(), player.getY() - turret.getY());
            Vector2 aimVector = new Vector2((float) Math.cos(currentAngle), (float) Math.sin(currentAngle));

            float angle = aimVector.angle(toPlayer);

        } else {
            //moving
            if(state == 0){
                currentAngle += rotationSpeed*delta;
                if(currentAngle < Constants.TURRET_MIN_ANGLE){
                    currentAngle = Constants.TURRET_MIN_ANGLE;
                    waitTimer = Constants.TURRET_WAIT_TIME;
                    state = 1;

                } else if(currentAngle > Constants.TURRET_MAX_ANGLE){
                    currentAngle = Constants.TURRET_MAX_ANGLE;
                    rotationSpeed = -rotationSpeed;
                    waitTimer = Constants.TURRET_WAIT_TIME;
                    state = 1;

                }

            //waiting
            } else if(state == 1){
                if(waitTimer <= 0){
                    rotationSpeed = -rotationSpeed;
                    state = 0;
                }
            }
        }
    }

    public void checkPlayerInSight(){
        Player player = GameScreen.level().getPlayer();
        Vector2 toPlayer = new Vector2(player.getX() - turret.getX(), player.getY() - turret.getY());
        Vector2 aimVector = new Vector2((float) Math.cos(currentAngle), (float) Math.sin(currentAngle));

        float angle = aimVector.angle(toPlayer);
        if(Math.abs(angle) < Constants.TURRET_ANG_RADIUS){
            playerInSight = true;
        } else {
            playerInSight = false;
        }
    }

    /*
    Getters and setters
     */

    public void setPlayerInRange(boolean inRange){
        playerInRange = inRange;
    }
}
