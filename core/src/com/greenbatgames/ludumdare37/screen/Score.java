package com.greenbatgames.ludumdare37.screen;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Quiv on 11-12-2016.
 */

public class Score {

    Map<Integer, Float> scores;

    public Score() {
        scores = new HashMap<Integer, Float>(GameScreen.getInstance().getTotalNumberLevels());
    }

    // Sets the record low time for a level. If there is no previous time, save the passed
    // time. Otherwise, compare to the last time and save the lowest
    public void setScore(int levelNumber, float elapsedTime) {
        Integer num = levelNumber;
        Float time = elapsedTime;

        if (scores.containsKey(num)) {
            Float previousTime = scores.get(num);

            if (time.compareTo(previousTime) < 0)
                scores.put(num, time);
        } else {
            scores.put(num, time);
        }
    }
}
