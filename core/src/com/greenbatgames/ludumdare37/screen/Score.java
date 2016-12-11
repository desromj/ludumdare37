package com.greenbatgames.ludumdare37.screen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Quiv on 11-12-2016.
 */

public class Score {

    List<Integer> levels;
    List<Float> scores;

    List<Float> topScores;

    public Score() {
        levels = new ArrayList<Integer>(GameScreen.getInstance().getTotalNumberLevels());
        scores = new ArrayList<Float>(GameScreen.getInstance().getTotalNumberLevels());
        topScores = new ArrayList<Float>(GameScreen.getInstance().getTotalNumberLevels());
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

    public List<Float> getTopScores() {
        return topScores;
    }

    public void reset() {
        if (topScores.isEmpty()) {
            topScores.addAll(scores);
        } else {
            for (int i = 0; i < scores.size(); i++) {
                if (scores.get(i).compareTo(topScores.get(i)) < 0)
                    topScores.set(i, scores.get(i));
            }
        }

        for (int i = 0; i < scores.size(); i++)
            scores.set(i, 999999f);
    }

    public boolean beatTopScore(int idx) {
        if (topScores.isEmpty()) return false;
        return (scores.get(idx).compareTo(topScores.get(idx)) < 0);
    }
}
