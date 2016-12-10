package com.greenbatgames.ludumdare37.level;

/**
 * Created by Quiv on 09-12-2016.
 */

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.greenbatgames.ludumdare37.entity.Platform;
import com.greenbatgames.ludumdare37.threat.*;
import com.greenbatgames.ludumdare37.util.Constants;

public class LevelLoader {
    private LevelLoader() {}

    // TODO: Can be passed parameters to load resources from outside editors
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
                            //TODO: Build the dimensions into the classes?
                            loadedLevel.stage.addActor(new Ghost(e.x, e.y, 20f, 20f, loadedLevel.world));
                        } else if(name.compareTo("goon") == 0) {
                            loadedLevel.stage.addActor(new Goon(e.x, e.y, 2*Constants.PLAYER_RADIUS * 2f,Constants.PLAYER_RADIUS * 4f, loadedLevel.world));
                        } else if(name.compareTo("turret") == 0){
                            loadedLevel.stage.addActor(new Turret(e.x, e.y, 20f, 20f, loadedLevel.world));
                        }
                    } else if(object instanceof RectangleMapObject){
                        Rectangle r = ((RectangleMapObject) object).getRectangle();
                        if(name.compareTo("lasergrid") == 0){
                            loadedLevel.stage.addActor(new LaserGrid(r.x, r.y, r.width, r.height, loadedLevel.world));
                        } else if(name.compareTo("spikes") == 0){
                            loadedLevel.stage.addActor(new Spike(r.x, r.y, r.width, r.height, loadedLevel.world));
                        }
                    }
                }
            }
        }

        return loadedLevel;
    }
}
