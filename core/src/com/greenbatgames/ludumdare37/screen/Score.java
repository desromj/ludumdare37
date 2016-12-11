package com.greenbatgames.ludumdare37.screen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quiv on 11-12-2016.
 */

public class Score {

    List<Integer> levels;
    List<Float> scores;

    public Score() {
        levels = new ArrayList<Integer>(GameScreen.getInstance().getTotalNumberLevels());
        scores = new ArrayList<Float>(GameScreen.getInstance().getTotalNumberLevels());
    }

    // Sets the record low time for a level. If there is no previous time, save the passed
    // time. Otherwise, compare to the last time and save the lowest
    public void setScore(int levelNumber, float elapsedTime) {

        int idx = -1;

        for (int i = 0; i < levels.size(); i++) {
            if (levelNumber == levels.get(i)) {
                idx = i;
                break;
            }
        }

        if (idx < 0) {
            levels.add(levelNumber);
            scores.add(elapsedTime);
        } else {
            float prevTime = scores.get(idx);

            if (elapsedTime < prevTime)
                scores.set(idx, elapsedTime);
        }
    }

    public List<Integer> getLevels() {
        return levels;
    }

    public List<Float> getScores() {
        return scores;
    }
}
