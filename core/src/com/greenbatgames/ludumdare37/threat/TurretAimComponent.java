package com.greenbatgames.ludumdare37.threat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.greenbatgames.ludumdare37.entity.Platform;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;
import com.greenbatgames.ludumdare37.util.DareSounds;

/**
 * Created by arne on 10-12-2016.
 */

public class TurretAimComponent implements Initializable {
    Turret turret;

    private float range;
    boolean playerInRange;
    boolean playerInSight;
    boolean playerInCrosshairs;
    boolean fired;

    private float waitTime;
    private float waitTimer;

    private float currentAngle;
    private float minAngle;
    private float maxAngle;
    private float rotationSpeed;

    boolean isFixed;


    float aimTimer;
    float beepTimer;
    boolean highBeepDone;

    Sound beepHighSound;
    Sound beepLowSound;
    Sound chargeSound;
    Sound dischargeSound;
    Sound fireSound;

    State state;

    private enum State {
        MOVING,
        WAITING
    }

    public TurretAimComponent(Turret turret){
        this.turret = turret;
        init();
    }

    public void init(){
        range = Constants.TURRET_RANGE;
        playerInRange = false;
        playerInSight = false;
        fired = false;

        waitTime = Constants.TURRET_WAIT_TIME;
        waitTimer = 0;
        aimTimer = 0;
        beepTimer = MathUtils.random(0, Constants.TURRET_BEEP_TIME);

        beepHighSound = DareSounds.BEEPHIGH.getSound();
        beepLowSound = DareSounds.BEEPLOW.getSound();
        chargeSound = DareSounds.CHARGE.getSound();
        dischargeSound = DareSounds.DISCHARGE.getSound();
        fireSound = DareSounds.FIRE.getSound();

        currentAngle = 0;
        minAngle = Constants.TURRET_MIN_ANGLE;
        maxAngle = Constants.TURRET_MAX_ANGLE;
        rotationSpeed = Constants.TURRET_ROTATION_SPEED;
        state = State.MOVING;
    }

    public void update(float delta){
        waitTimer -= delta;
        if(playerInRange) {
            checkPlayerInSight();
        } else {
            playerInSight = false;
        }

        Player player = GameScreen.level().getPlayer();
        if(playerInSight){
            if(!highBeepDone) {
                chargeSound.stop();
                beepHighSound.play(DareSounds.BEEPHIGH.getVolume());
                chargeSound.play(DareSounds.CHARGE.getVolume());
                highBeepDone = true;
            }
            aimTimer -= delta;
            if(aimTimer <= 0 && playerInCrosshairs){
                if(!fired){
                    chargeSound.stop();
                    dischargeSound.play(DareSounds.DISCHARGE.getVolume());
                    fireSound.play(DareSounds.FIRE.getVolume());
                    turret.touchPlayer(GameScreen.level().getPlayer());
                    fired = true;
                }

            } else {
                //Follow player as long as line of sight is held
                Vector2 toPlayer = new Vector2(player.getX() - turret.getX(), player.getY() - turret.getY());
                Vector2 aimVector = new Vector2((float) Math.cos(currentAngle), (float) Math.sin(currentAngle));

                float angle = aimVector.angleRad(toPlayer);
                currentAngle += MathUtils.clamp(angle, -Constants.TURRET_ROTATION_SPEED_FAST*delta, Constants.TURRET_ROTATION_SPEED_FAST*delta);
            }

        } else {
            aimTimer = Constants.TURRET_AIM_TIME;
            if(highBeepDone){
                chargeSound.stop();
                dischargeSound.play(DareSounds.DISCHARGE.getVolume());
            }
            highBeepDone = false;
            if(isFixed){
                Vector2 toPlayer = new Vector2((float) Math.cos(minAngle), (float) Math.sin(minAngle));
                Vector2 aimVector = new Vector2((float) Math.cos(currentAngle), (float) Math.sin(currentAngle));

                float angle = aimVector.angleRad(toPlayer);
                currentAngle += MathUtils.clamp(angle, -rotationSpeed*delta, rotationSpeed*delta);
            } else {
                //moving
                if (state == State.MOVING) {
                    currentAngle += rotationSpeed * delta;
                    if (currentAngle < minAngle) {
                        currentAngle = minAngle;
                        waitTimer = waitTime;
                        state = State.WAITING;

                    } else if (currentAngle > maxAngle) {
                        currentAngle = maxAngle;
                        waitTimer = waitTime;
                        state = State.WAITING;

                    }

                    //waiting
                } else if (state == State.WAITING) {
                    if (waitTimer <= 0) {
                        rotationSpeed = -rotationSpeed;
                        state = State.MOVING;
                    }
                }
            }
        }
        turret.setAim(currentAngle);


        beepTimer -= delta;
        if(beepTimer < 0){
            beepTimer = Constants.TURRET_BEEP_TIME;
            beepLowSound.play(DareSounds.BEEPLOW.getVolume());
        }
    }

    public void checkPlayerInSight(){
        playerInCrosshairs = false;
        Vector2 origin = turret.getBody().getPosition();
        Vector2 ray = new Vector2(range*MathUtils.cos(currentAngle), range*MathUtils.sin(currentAngle));
        ray.add(origin);
        Vector2 playerPos = GameScreen.level().getPlayer().getBody().getPosition();

        GameScreen.level().getWorld().rayCast(makeInSightCallback(), origin, playerPos);
        GameScreen.level().getWorld().rayCast(makeCrosshairsCallback(), origin, ray);
    }

    private RayCastCallback makeCrosshairsCallback(){
        return new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                Object o = fixture.getBody().getUserData();
                if(o instanceof Platform){
                    playerInCrosshairs = false;
                    return fraction;
                } else if (o instanceof Player){
                    playerInCrosshairs = true;
                    return fraction;
                }
                return -1;
            }
        };
    }

    private RayCastCallback makeInSightCallback(){
        return new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                Object o = fixture.getBody().getUserData();
                if(o instanceof Platform){
                    playerInSight = false;
                    return fraction;
                } else if (o instanceof Player){
                    playerInSight = true;
                    return fraction;
                }
                return -1;
            }
        };
    }

    /*
    Getters and setters
     */

    public float getCurrentAngle() { return currentAngle; }

    public float getPercentCharged() {

        if (!playerInSight) return 0;

        float ratio = (Constants.TURRET_AIM_TIME - (Constants.TURRET_AIM_TIME - aimTimer))
                / Constants.TURRET_AIM_TIME;
        return 1.0f - ratio;
    }

    public float getWaitTime(){
        return waitTime;
    }

    public void setFixed(boolean fixed){
        this.isFixed = fixed;
    }

    public void setFixedAngle(float fixedAngle){
        this.minAngle = fixedAngle;
        this.maxAngle = fixedAngle;
        currentAngle = fixedAngle;
    }

    public void setRange(float range){
        this.range = range;
    }

    public void setRotationSpeed(float rotationSpeed){
        this.rotationSpeed = rotationSpeed;
    }

    public void setWaitTime(float waitTime){
        this.waitTime = waitTime;
    }

    public void setWaitTimer(float time) {
        waitTimer = time;
    }
}
