package com.greenbatgames.ludumdare37.level;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Quiv on 10-12-2016.
 */

// Handle the array of levels to be loaded through resource files
public class Levels {

    private List<String> list;
    private int currentLevelIdx;

    public Levels() {
        list = new LinkedList<String>();
        currentLevelIdx = 0;
        loadList();
    }

    private void loadList() {
        list.add("maps/map5.tmx");
        list.add("maps/map2.tmx");
        list.add("maps/map3.tmx");
        list.add("maps/map4.tmx");
        list.add("maps/map5.tmx");
    }

    public String currentResource() {
        return list.get(currentLevelIdx);
    }

    // Increments the current level index counter
    public String nextResource() {
        if (currentLevelIdx + 1 >= list.size())
            currentLevelIdx = 0;
        else
            currentLevelIdx++;

        return currentResource();
    }

    public boolean hasNextLevel() {
        return (currentLevelIdx + 1 < list.size());
    }

    public int getCurrentLevelNumber() { return currentLevelIdx + 1; }
}
