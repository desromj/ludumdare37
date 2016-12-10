package com.greenbatgames.ludumdare37.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.greenbatgames.ludumdare37.collision.DareContactListener;
import com.greenbatgames.ludumdare37.entity.Platform;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.threat.Goon;
import com.greenbatgames.ludumdare37.util.Constants;

/**
 * Created by Quiv on 09-12-2016.
 */

// TODO: All game logic goes here
public class Level implements Initializable {

    World world;
    Stage stage;

    Box2DDebugRenderer debugRenderer;
    Matrix4 debugMatrix;

    Player player;
    //Platform platform;

    public Level() {
        init();
    }

    @Override public void init() {
        world = new World(Constants.GRAVITY, true);
        world.setContactListener(new DareContactListener());
        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));

        debugRenderer = new Box2DDebugRenderer();

        // TODO: Testing values, replace with LevelLoading
        player = new Player(
                20f,
                40f,
                Constants.PLAYER_RADIUS * 2f,
                Constants.PLAYER_RADIUS * 4f,
                world);

        //platform = new Platform(0f, 0f, 400f, 40f, world);

        stage.addActor(player);
        //stage.addActor(platform);
    }

    public void render(float delta) {

        // Update logic
        world.step(delta, 6, 6);
        stage.act(delta);

        // Prepare viewports and projection matricies
        stage.getViewport().apply();

        // Scale the debug Matrix to box2d sizes
        debugMatrix = stage.getViewport().getCamera().combined.cpy().scale(
                Constants.PTM,
                Constants.PTM,
                0
        );

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render
        stage.draw();

        // Render the debug physics engine settings
        debugRenderer.render(world, debugMatrix);
    }

    // Getters and Setters
    public Player getPlayer(){
        return player;
    }

    public Viewport getViewport()
    {
        return stage.getViewport();
    }

    public void addActorToStage(Actor actor) { stage.addActor(actor); }

    private void reinitializeAllActors()
    {
        for (Actor actor: stage.getActors())
            if (actor instanceof Initializable)
                ((Initializable) actor).init();
    }
}
