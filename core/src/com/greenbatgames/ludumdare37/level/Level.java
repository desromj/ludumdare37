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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.ludumdare37.collision.DareContactListener;
import com.greenbatgames.ludumdare37.entity.Background;
import com.greenbatgames.ludumdare37.entity.DareLight;
import com.greenbatgames.ludumdare37.hud.EndLevelHUD;
import com.greenbatgames.ludumdare37.hud.GameHUD;
import com.greenbatgames.ludumdare37.hud.RestartHUD;
import com.greenbatgames.ludumdare37.iface.Disposable;
import com.greenbatgames.ludumdare37.iface.Initializable;
import com.greenbatgames.ludumdare37.player.Player;
import com.greenbatgames.ludumdare37.threat.Ghost;
import com.greenbatgames.ludumdare37.util.Constants;

import java.util.LinkedList;
import java.util.List;

import box2dLight.RayHandler;

/**
 * Created by Quiv on 09-12-2016.
 */

// All game logic goes here
public class Level implements Initializable, Disposable {

    World world;
    Stage stage;

    RayHandler rayHandler;
    List<DareLight> lights;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    Background background;
    Player player;
    RestartHUD restartHUD;
    GameHUD gameHUD;
    EndLevelHUD endLevelHUD;

    public Level() {
        init();
    }

    @Override public void init() {
        world = new World(Constants.GRAVITY, true);
        world.setContactListener(new DareContactListener());

        stage = new Stage(new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT));
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.2f);
        lights = new LinkedList<DareLight>();

        // Initialize the first few objects in every level
        background = new Background("graphics/bg.jpg");

        player = new Player(
                40f,
                80f,
                Constants.PLAYER_RADIUS * 2f,
                Constants.PLAYER_RADIUS * 4f,
                world);
        restartHUD = new RestartHUD();
        gameHUD = new GameHUD();
        endLevelHUD = new EndLevelHUD();

        stage.addActor(player);
    }

    public void render(float delta) {

        // Update logic
        world.step(delta, 6, 6);
        stage.act(delta);

        // Prepare viewports and projection matricies
        stage.getViewport().apply();
        tiledMapRenderer.setView((OrthographicCamera) stage.getViewport().getCamera());

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render
        background.render();
        tiledMapRenderer.render();
        stage.draw();

        // Scale the rayHandler to Box2D values
        rayHandler.setCombinedMatrix(stage.getViewport().getCamera().combined.cpy().scale(
                Constants.PTM,
                Constants.PTM,
                1));
        rayHandler.updateAndRender();
      
        gameHUD.act(delta);
        restartHUD.act(delta);
        endLevelHUD.act(delta);

        // Render HUDs after the main game world
        stage.getBatch().begin();
        gameHUD.draw(stage.getBatch(), 1f);
        restartHUD.draw(stage.getBatch(), 1f);
        endLevelHUD.draw(stage.getBatch(), 1f);
        stage.getBatch().end();
    }

    // Getters and Setters
    public Player getPlayer(){
        return player;
    }

    public World getWorld(){
        return world;
    }

    public RayHandler getRayHandler(){
        return rayHandler;
    }

    public Viewport getViewport() {
        return stage.getViewport();
    }

    public float getLevelElapsedTime() {
        return gameHUD.getElapsedTime();
    }

    public void addActorToStage(Actor actor) { stage.addActor(actor); }

    private void reinitializeAllActors()
    {
        for (Actor actor: stage.getActors())
            if (actor instanceof Initializable)
                ((Initializable) actor).init();
    }

    public void killPlayer() {

        if (!endLevelHUD.isVisible()) {
            player.setDead(true);
            restartHUD.show();
        }

        stopTimer();
    }

    public void showEndLevelHUD() {
        if (!restartHUD.isVisible())
            endLevelHUD.show();
    }

    public void stopTimer() {
        gameHUD.setStopTimer(true);
    }

    // When loading a level, set the TiledMap reference so the level can render it later
    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    public DareLight addLight(float x, float y){
        DareLight l = new DareLight(x, y, rayHandler, world);
        lights.add(l);
        stage.addActor(l);
        return l;
    }

    // Moves player to the end of the stage, ensuring it is rendered last, over all other actors (except HUDs)
    public void movePlayerToEnd() {

        boolean found = false;

        for (Actor actor: stage.getActors()) {
            if (actor == player) {
                found = true;
                player.remove();
                break;
            }
        }

        if (found) {
            stage.addActor(player);
        }
    }

    // Dispose lights and actor to prepare for next level
    @Override
    public void dispose() {
        rayHandler.removeAll();
        rayHandler.dispose();
        for (Actor actor: stage.getActors()) {
            if (actor instanceof Disposable){
                if (actor instanceof Ghost) {
                    Gdx.app.log("level", "found ghost");
                }
                ((Disposable) actor).dispose();
            }
        }
    }
}
