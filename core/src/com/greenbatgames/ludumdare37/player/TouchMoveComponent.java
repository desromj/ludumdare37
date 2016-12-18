package com.greenbatgames.ludumdare37.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.util.Constants;
import com.greenbatgames.ludumdare37.util.DareSounds;

/**
 * Created by Quiv on 17-12-2016.
 */

public class TouchMoveComponent extends KeyboardMoveComponent {

    public static final String TAG = TouchMoveComponent.class.getSimpleName();

    protected boolean shouldDash, shouldWalk, shouldJump;
    protected Vector2 lastTouchDown;

    public TouchMoveComponent(Player player) {
        super(player);
        shouldDash = false;
        shouldWalk = false;
        shouldJump = false;
        lastTouchDown = new Vector2(-1f, -1f);
    }

    // TODO: Replace keyboard controls in update() with touch controls
    @Override
    public boolean update(float delta) {
        cannotJumpFor -= delta;
        disableCollisionFor -= delta;
        cannotDashFor -= delta;
        dashCooldown -= delta;

        // Horizontal moving movement
        Body body = player.getBody();

        // check to see if we should dash first
        if (canDash()) {
            cannotDashFor = Constants.PLAYER_DASH_DURATION;
            dashCooldown = Constants.PLAYER_DASH_COOLDOWN;
        }

        // Handle dashing, until finished
        if (isDashing()) {
            if (facingRight) {
                body.setLinearVelocity(
                        Constants.PLAYER_DASH_SPEED,
                        (body.getLinearVelocity().y >= 0f) ? body.getLinearVelocity().y : 0f);
            } else {
                body.setLinearVelocity(
                        -Constants.PLAYER_DASH_SPEED,
                        (body.getLinearVelocity().y >= 0f) ? body.getLinearVelocity().y : 0f);
            }
        } else if (shouldWalk) {
            walkSoundTimer -= delta;
            if(isOnGround() && walkSoundTimer < 0){
                walkSoundTimer = Constants.WALK_SOUND_TIME;
                int s = MathUtils.floor(MathUtils.random(1, 4.99f));
                if(s == 1){
                    walkSound1.play(DareSounds.STEP1.getVolume());
                } else if(s == 2){
                    walkSound2.play(DareSounds.STEP2.getVolume());
                } else if(s == 3){
                    walkSound3.play(DareSounds.STEP3.getVolume());
                } else if(s == 4){
                    walkSound4.play(DareSounds.STEP4.getVolume());
                }
            }

            // Handle movement (left/right)
            Vector2 playerUnproj = GameScreen.level().getViewport().unproject(new Vector2(
                    player.getX(), player.getY()));

            if (playerUnproj.x < lastTouchDown.x) {
                body.setLinearVelocity(
                        Constants.PLAYER_MOVE_SPEED,
                        body.getLinearVelocity().y);
                facingRight = true;
            } else if (playerUnproj.x > lastTouchDown.x) {
                body.setLinearVelocity(
                        -Constants.PLAYER_MOVE_SPEED,
                        body.getLinearVelocity().y);
                facingRight = false;
            }
        } else {
            walkSoundTimer = 0;
            body.setLinearVelocity(
                    body.getLinearVelocity().x * Constants.HORIZONTAL_MOVE_DAMPEN,
                    body.getLinearVelocity().y);
        }

        // Add impulse for single jump
        if (canJump() && shouldJump) {

            shouldJump = false;
            body.setLinearVelocity(
                    body.getLinearVelocity().x,
                    0f
            );

            body.applyLinearImpulse(
                    Constants.PLAYER_JUMP_IMPULSE,
                    body.getPosition(),
                    true
            );

            if (numFootContacts == 0)
                doubleJumped = true;
        }

        return true;
    }

    @Override
    public boolean canDash() {

        if (!shouldDash) {
            return false;
        }

        shouldDash = false;

        return dashCooldown < 0.0f;
    }

    // TODO: Touch controls
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (!shouldWalk)
            lastTouchDown.set(screenX, screenY);

        // Jump boxes are the bottom left and right 10% of the screen
        Viewport viewport = GameScreen.level().getViewport();
        float squareUnit = viewport.getScreenWidth() / 10f;

        if ((screenY > viewport.getScreenHeight() - squareUnit)
            && ((screenX < squareUnit)
                        || (screenX > viewport.getScreenWidth() - squareUnit))) {
            shouldJump = true;
        } else {
            shouldWalk = true;
        }

        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        shouldWalk = false;
        shouldJump = false;
        return true;
    }

    public boolean touchDragged(int screenX, int screenY, int pointer) {

        // Minimum distance needed to dash
        Viewport viewport = GameScreen.level().getViewport();
        float minDist = viewport.getScreenWidth() / 8f;

        float dist = Vector2.dst(lastTouchDown.x, lastTouchDown.y, screenX, screenY);

        if (dist > minDist)
            shouldDash = true;

        return true;
    }

}
