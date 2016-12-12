package com.greenbatgames.ludumdare37.level;

/**
 * Created by Quiv on 09-12-2016.
 */

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
import com.greenbatgames.ludumdare37.entity.ExitPoint;
import com.greenbatgames.ludumdare37.entity.Platform;
import com.greenbatgames.ludumdare37.threat.*;
import com.greenbatgames.ludumdare37.util.Constants;

public class LevelLoader {
    private LevelLoader() {}

    // Can be passed parameters to load resources from outside editors
    public static Level loadLevel(String filename) {
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

                        Platform platform = new Platform(   rect.getX(),
                                                            rect.getY(),
                                                            rect.getWidth(),
                                                            rect.getHeight(),
                                                            loadedLevel.world);

                        loadedLevel.stage.addActor(platform);
                    }
                }
                continue;
            }
            if(layer.getName().equals("objects")){
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
                        } else if(name.compareTo("light") == 0){
                            loadedLevel.addLight(e.x, e.y);
                        }
                    } else if(object instanceof RectangleMapObject){
                        Rectangle r = ((RectangleMapObject) object).getRectangle();
                        if(name.compareTo("exitpoint") == 0){
                            loadedLevel.stage.addActor(new ExitPoint(r.x, r.y - r.height/2f, r.width, r.height, loadedLevel.world));
                        } else if(name.compareTo("lasergrid") == 0) {
                            loadedLevel.stage.addActor(new LaserGrid(r.x, r.y, r.width, r.height, loadedLevel.world));
                        } else if(name.compareTo("mine") == 0){
                            loadedLevel.stage.addActor(new PressurePlate(r.x, r.y + Constants.PRESSURE_PLATE_HEIGHT/2f, r.width, Constants.PRESSURE_PLATE_HEIGHT, loadedLevel.world));
                        } else if(name.compareTo("spikes") == 0){
                            loadedLevel.stage.addActor(new Spike(r.x, r.y, r.width, r.height, loadedLevel.world));
                        } else if(name.compareTo("lava") == 0){
                            loadedLevel.stage.addActor(new Lava(r.x, r.y, r.width, r.height, loadedLevel.world));
                        }
                    } else if(object instanceof PolygonMapObject){
                        Polygon p = ((PolygonMapObject) object).getPolygon();
                        if(name.compareTo("turret") == 0){
                            Turret t = new Turret(
                                    p.getX(),
                                    p.getY() - Constants.TILE_WIDTH*2.5f,
                                    p.getBoundingRectangle().getWidth(),
                                    p.getBoundingRectangle().getHeight(),
                                    loadedLevel.world);
                            t.getAimer().setRange(object.getProperties().get("range", Constants.TURRET_RANGE, Float.class));
                            t.getAimer().setFixed(object.getProperties().get("fixed", false, Boolean.class));
                            t.getAimer().setFixedAngle(object.getProperties().get("fixedAngle", Constants.TURRET_MIN_ANGLE, Float.class)*MathUtils.degRad);
                            t.getAimer().setWaitTime(object.getProperties().get("waitTime", Constants.TURRET_WAIT_TIME, Float.class));
                            t.getAimer().setRotationSpeed(object.getProperties().get("rotationSpeed", Constants.TURRET_ROTATION_SPEED, Float.class));

                            loadedLevel.stage.addActor(t);
                        }
                    }
                }
            }
        }

        return loadedLevel;
    }
}
