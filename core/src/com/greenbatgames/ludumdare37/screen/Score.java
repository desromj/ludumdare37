package com.greenbatgames.ludumdare37.screen;

import com.badlogic.gdx.Gdx;

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

        for (int i = 0; i < GameScreen.getInstance().getTotalNumberLevels(); i++) {
            levels.add(i+1);
            scores.add(999999f);
            topScores.add(999999f);
        }
    }

    // Sets the record low time for a level. If there is no previous time, save the passed
    // time. Otherwise, compare to the last time and save the lowest
    public void setScore(int levelNumber, float elapsedTime) {

        int idx = -1;

        // Find the matching idx of the level to update
        for (int i = 0; i < levels.size(); i++) {
            if (levelNumber == levels.get(i)) {
                idx = i;
                break;
            }
        }

        // Set the current score
        scores.set(idx, elapsedTime);

        // Set the top score if the new score is better, to save on retries
        if (elapsedTime < topScores.get(idx))
            topScores.set(idx, elapsedTime);
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
        for (int i = 0; i < scores.size(); i++)
            scores.set(i, 999999f);
    }

    public boolean beatTopScore(int idx) {
        return (scores.get(idx).compareTo(topScores.get(idx)) <= 0);
    }
}
