package com.greenbatgames.ludumdare37.level;

/**
 * Created by Quiv on 09-12-2016.
 */

public class LevelLoader {
    private LevelLoader() {}

    // TODO: Can be passed parameters to load resources from outside editors
    public static Level loadLevel() {
        return new Level();
    }
}
