package com.greenbatgames.ludumdare37.level;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Quiv on 10-12-2016.
 */

// Handle the array of levels to be loaded through resource files
public class Levels {

    private List<String> list;
    private List<String> tooltips;
    private int currentLevelIdx;

    public Levels() {
        list = new LinkedList<String>();
        tooltips = new LinkedList<String>();
        currentLevelIdx = 0;
        loadList();
    }

    private void loadList() {
        list.add("maps/map1.tmx");
        tooltips.add("Press Left/Right Arrows to move");
        list.add("maps/map2.tmx");
        tooltips.add("Press Z to jump");
        list.add("maps/map3.tmx");
        tooltips.add("Press Z while in air to double-jump");
        list.add("maps/map4.tmx");
        tooltips.add("");
        list.add("maps/map5.tmx");
        tooltips.add("");
        list.add("maps/map6.tmx");
        tooltips.add("Press X to dash");
        list.add("maps/map7.tmx");
        tooltips.add("");
        list.add("maps/map8.tmx");
        tooltips.add("");
        list.add("maps/map9.tmx");
        tooltips.add("");
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
    public String currentTooltip() { return tooltips.get(currentLevelIdx); }
    public boolean hasCurrentTooltip() { return tooltips.get(currentLevelIdx).compareTo("") != 0; }

    public int size() { return list.size(); }
}
