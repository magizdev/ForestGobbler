package com.magizdev.common.lib;


public class Score {
	private final static int LEVEL_UP = 5;
	private final static int MAX_LEVEL = 5;
	private final static int THRESHOLD = 2500;

	private int score = 0;
	private int level = 0;
	private int accumulate = 0;
	private long lastScroeDate = System.currentTimeMillis();
	private int totalCalled = 0;
	private int successCalled = 0;
	private IScoreListener scoreListener;

	public int Add() {
		totalCalled++;
		long scoreDate = System.currentTimeMillis();
		if (scoreDate - lastScroeDate < THRESHOLD) {
			levelUp();
			successCalled++;
		} else {
			levelClear();
		}
		lastScroeDate = scoreDate;

		int incremental = computeScore();
		score += incremental;
		if (scoreListener != null) {
			scoreListener.OnScoreChanged(score);
		}
		return incremental;
	}
	
	public float GetRating(){
		if(totalCalled == 0){
			return 0;
		}else {
			return successCalled/(float)totalCalled;
		}
	}

	public void Clear() {
		score = level = accumulate = totalCalled = successCalled = 0;
		if (scoreListener != null) {
			scoreListener.OnScoreChanged(score);
		}
		lastScroeDate = System.currentTimeMillis();
	}

	private void levelClear() {
		level = 0;
		accumulate = 0;
	}

	private void levelUp() {
		if (accumulate < LEVEL_UP) {
			accumulate++;
		}
		if (accumulate == LEVEL_UP) {
			if (level < MAX_LEVEL) {
				level++;
				accumulate = 0;
			}
		}
	}

	private int computeScore() {
		int baseScore = 10;
		int levelExtra = 10 * level + accumulate;
		int maxLevelExtra = (level == MAX_LEVEL) ? 20 : 0;
		return (baseScore + levelExtra + maxLevelExtra) * 22;
	}

	public int getScore() {
		return score;
	}

	public void setScoreListener(IScoreListener scoreListener) {
		this.scoreListener = scoreListener;
	}
}
