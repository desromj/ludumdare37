package com.greenbatgames.ludumdare37.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.greenbatgames.ludumdare37.collision.DareContactListener;
import com.greenbatgames.ludumdare37.hud.GameHUD;
import com.greenbatgames.ludumdare37.hud.RestartHUD;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.threat.PressurePlate;
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

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    Player player;
    PressurePlate plate;
    RestartHUD restartHUD;
    GameHUD gameHUD;

    public Level() {
        init();
    }

    @Override public void init() {
        world = new World(Constants.GRAVITY, true);
        world.setContactListener(new DareContactListener());
        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));

        debugRenderer = new Box2DDebugRenderer();

        // TODO: Move player to spawn points from map editor
        player = new Player(
                40f,
                80f,
                Constants.PLAYER_RADIUS * 2f,
                Constants.PLAYER_RADIUS * 4f,
                world);
        plate = new PressurePlate(1200f, 32f, 32, Constants.PRESSURE_PLATE_HEIGHT, world);
        restartHUD = new RestartHUD();
        gameHUD = new GameHUD();
      
        stage.addActor(player);
        stage.addActor(plate);
        stage.addActor(restartHUD);
        stage.addActor(gameHUD);
    }

    public void render(float delta) {

        // Update logic
        world.step(delta, 6, 6);
        stage.act(delta);

        // Prepare viewports and projection matricies
        stage.getViewport().apply();
        tiledMapRenderer.setView((OrthographicCamera) stage.getViewport().getCamera());

        // Scale the debug Matrix to box2d sizes
        debugMatrix = stage.getViewport().getCamera().combined.cpy().scale(
                Constants.PTM,
                Constants.PTM,
                0
        );

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render
        tiledMapRenderer.render();
        stage.draw();

        // Render the debug physics engine settings
        debugRenderer.render(world, debugMatrix);
    }

    // Getters and Setters
    public Player getPlayer(){
        return player;
    }

    public World getWorld(){
        return world;
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

    public void killPlayer() {
        player.setDead(true);
        restartHUD.show();
        stopTimer();
    }

    public void stopTimer() {
        gameHUD.setStopTimer(true);
    }

    // When loading a level, set the TiledMap reference so the level can render it later
    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }
}
