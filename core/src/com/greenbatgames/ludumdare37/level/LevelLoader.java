package com.greenbatgames.ludumdare37.level;

/**
 * Created by Quiv on 09-12-2016.
 */

import com.badlogic.gdx.Gdx;
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

import java.util.Iterator;

public class LevelLoader {
    private LevelLoader() {}

    // TODO: Can be passed parameters to load resources from outside editors
    public static Level loadLevel(String filename) {
        //Load tmx file into TiledMap object
        TiledMap tiledMap = new TmxMapLoader().load(filename);

        //Create new level object to load level data into
        Level loadedLevel = new Level();

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
                    String name = object.getProperties().get("type", String.class);

                    if(object instanceof EllipseMapObject){
                        Ellipse e = ((EllipseMapObject) object).getEllipse();
                        if(name.equals("ghost")){
                            //TODO: Build the dimensions into the classes?
                            loadedLevel.stage.addActor(new Ghost(e.x, e.y, 20f, 20f, loadedLevel.world));
                        } else if(name.equals("goon")) {
                            loadedLevel.stage.addActor(new Goon(e.x, e.y, 2*Constants.PLAYER_RADIUS * 2f,Constants.PLAYER_RADIUS * 4f, loadedLevel.world));
                        }
                    }
                }
            }
        }

        return loadedLevel;
    }
}
