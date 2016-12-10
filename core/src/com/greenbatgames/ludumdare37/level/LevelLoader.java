package com.greenbatgames.ludumdare37.level;

/**
 * Created by Quiv on 09-12-2016.
 */

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.greenbatgames.ludumdare37.entity.Platform;

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
            }
        }

        return loadedLevel;
    }
}
