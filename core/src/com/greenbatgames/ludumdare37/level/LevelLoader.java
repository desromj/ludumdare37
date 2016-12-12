package com.greenbatgames.ludumdare37.level;

/**
 * Created by Quiv on 09-12-2016.
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.greenbatgames.ludumdare37.entity.DareLight;
import com.greenbatgames.ludumdare37.entity.ExitPoint;
import com.greenbatgames.ludumdare37.entity.Platform;
import com.greenbatgames.ludumdare37.screen.GameScreen;
import com.greenbatgames.ludumdare37.threat.*;
import com.greenbatgames.ludumdare37.util.Constants;
import com.sun.corba.se.impl.orbutil.closure.Constant;

public class LevelLoader {
    private LevelLoader() {}

    // Can be passed parameters to load resources from outside editors
    public static Level loadLevel(String filename) {
        if(GameScreen.level() != null){

        }

        //Load tmx file into TiledMap object
        TiledMap tiledMap = new TmxMapLoader().load(filename);

        //Create new level object to load level data into
        Level loadedLevel = new Level();
        loadedLevel.setTiledMap(tiledMap);

        //Populate the map with the relevant objects
        for (MapLayer layer : tiledMap.getLayers()){

            if(layer.getName().equals("collision")){
                for (MapObject object : layer.getObjects()){
                    if(object instanceof RectangleMapObject){
                        Rectangle rect = ((RectangleMapObject) object).getRectangle();

                        Platform platform = new Platform(
                                rect.getX(),
                                rect.getY(),
                                rect.getWidth(),
                                rect.getHeight(),
                                loadedLevel.world);

                        loadedLevel.stage.addActor(platform);
                    }
                }
            } else if(layer.getName().equals("objects")){
                for(MapObject object : layer.getObjects()){
                    String name = object.getName();

                    if(object instanceof EllipseMapObject){
                        Ellipse e = ((EllipseMapObject) object).getEllipse();
                        if(name.compareTo("gas") == 0){
                            loadedLevel.stage.addActor(new PoisonGas(true));
                        } if(name.compareTo("ghost") == 0){
                            Ghost g = new Ghost(e.x, e.y, 20f, 20f, loadedLevel.world);

                            g.setWallhack(object.getProperties().get("wallhack", false, Boolean.class));

                            loadedLevel.stage.addActor(g);
                        } else if(name.compareTo("goon") == 0) {
                            loadedLevel.stage.addActor(new Goon(e.x, e.y, 2*Constants.PLAYER_RADIUS * 2f,Constants.PLAYER_RADIUS * 4f, loadedLevel.world));
                        } else if(name.compareTo("start") == 0){
                            loadedLevel.getPlayer().setPosition(e.x, e.y);
                        }
                    } else if(object instanceof RectangleMapObject){
                        Rectangle r = ((RectangleMapObject) object).getRectangle();
                        if(name.compareTo("exitpoint") == 0){
                            loadedLevel.stage.addActor(new ExitPoint(r.x, r.y - r.height/2f, r.width, r.height, loadedLevel.world));
                        } else if(name.compareTo("lasergrid") == 0) {
                            LaserGrid l = new LaserGrid(r.x, r.y, r.width, r.height, loadedLevel.world, loadedLevel.rayHandler);

                            l.setOnPeriod(object.getProperties().get("onTime", Constants.LASERGRID_ON_TIME, Float.class));
                            l.setOffPeriod(object.getProperties().get("offTime", Constants.LASERGRID_OFF_TIME, Float.class));
                            l.setWarmupTime(object.getProperties().get("warmupTime", Constants.LASERGRID_WARMUP_TIME, Float.class));
                            l.setTimeUntilSwitch(/*l.getOnPeriod() - */object.getProperties().get("timeOffset", Constants.LASERGRID_OFF_TIME, Float.class));

                            loadedLevel.stage.addActor(l);
                        } else if(name.compareTo("mine") == 0){
                            loadedLevel.stage.addActor(new PressurePlate(r.x, r.y + Constants.PRESSURE_PLATE_HEIGHT/2f, r.width, Constants.PRESSURE_PLATE_HEIGHT, loadedLevel.world));
                        } else if(name.compareTo("spikes") == 0){
                            loadedLevel.stage.addActor(new Spike(r.x, r.y, r.width, r.height, loadedLevel.world));
                        } else if(name.compareTo("lava") == 0){
                            loadedLevel.stage.addActor(new Lava(r.x, r.y, r.width, r.height, loadedLevel.world, loadedLevel.rayHandler));
                        }
                    } else if(object instanceof PolygonMapObject){
                        Polygon p = ((PolygonMapObject) object).getPolygon();
                        if(name.compareTo("turret") == 0){
                            Turret t = new Turret(
                                    p.getX(),
                                    p.getY() - Constants.TILE_WIDTH*2.5f,
                                    p.getBoundingRectangle().getWidth(),
                                    p.getBoundingRectangle().getHeight(),
                                    loadedLevel.world,
                                    loadedLevel.rayHandler);
                            t.getAimer().setFixed(object.getProperties().get("fixed", false, Boolean.class));
                            t.getAimer().setMinAngle(object.getProperties().get("minAngle", Constants.TURRET_MIN_ANGLE, Float.class)*MathUtils.degRad);
                            t.getAimer().setMaxAngle(object.getProperties().get("maxAngle", Constants.TURRET_MAX_ANGLE, Float.class)*MathUtils.degRad);
                            t.getAimer().setFixedAngle(object.getProperties().get("fixedAngle", Constants.TURRET_MIN_ANGLE, Float.class)*MathUtils.degRad);
                            t.getAimer().setWaitTime(object.getProperties().get("waitTime", Constants.TURRET_WAIT_TIME, Float.class));
                            t.getAimer().setWaitTimer(t.getAimer().getWaitTime() - object.getProperties().get("timeOffset", 0f, Float.class));
                            t.getAimer().setRotationSpeed(object.getProperties().get("rotationSpeed", Constants.TURRET_ROTATION_SPEED, Float.class));

                            loadedLevel.stage.addActor(t);
                        } else if(name.compareTo("turretS") == 0){
                            TurretSniper t = new TurretSniper(
                                    p.getX(),
                                    p.getY() - Constants.TILE_WIDTH*2.5f,
                                    p.getBoundingRectangle().getWidth(),
                                    p.getBoundingRectangle().getHeight(),
                                    loadedLevel.world,
                                    loadedLevel.rayHandler);
                            t.getAimer().setFixed(object.getProperties().get("fixed", false, Boolean.class));
                            t.getAimer().setMinAngle(object.getProperties().get("minAngle", Constants.TURRET_MIN_ANGLE, Float.class)*MathUtils.degRad);
                            t.getAimer().setMaxAngle(object.getProperties().get("maxAngle", Constants.TURRET_MAX_ANGLE, Float.class)*MathUtils.degRad);
                            t.getAimer().setFixedAngle(object.getProperties().get("fixedAngle", Constants.TURRET_MIN_ANGLE, Float.class)*MathUtils.degRad);
                            t.getAimer().setWaitTime(object.getProperties().get("waitTime", Constants.TURRET_WAIT_TIME, Float.class));
                            t.getAimer().setWaitTimer(t.getAimer().getWaitTime() - object.getProperties().get("timeOffset", 0f, Float.class));
                            t.getAimer().setRotationSpeed(object.getProperties().get("rotationSpeed", Constants.TURRET_ROTATION_SPEED, Float.class));

                            loadedLevel.stage.addActor(t);
                        }
                    }
                }
            } else if(layer.getName().equals("lights")){
                for(MapObject object : layer.getObjects()) {
                    if(object instanceof EllipseMapObject){
                        Ellipse e = ((EllipseMapObject) object).getEllipse();
                        DareLight l = loadedLevel.addLight(e.x, e.y);
                        l.setFlickering(object.getProperties().get("flickering", false, Boolean.class));
                        l.setFlickerTimeOn(object.getProperties().get("onTime", Constants.LIGHTS_FLICKER_ON_TIME, Float.class));
                        l.setFlickerTimeOff(object.getProperties().get("offTime", Constants.LIGHTS_FLICKER_OFF_TIME, Float.class));
                    }
                }
            }
        }

        return loadedLevel;
    }
}
