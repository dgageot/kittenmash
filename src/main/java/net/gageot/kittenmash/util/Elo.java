package net.gageot.kittenmash.util;

import javax.inject.Singleton;

@Singleton
public class Elo {
	private static final int MAX = 10;
	private static final int START_SCORE = 1000;

	private final int[] playedPerKitten;
	private final int[] scorePerKitten;

	public Elo() {
		playedPerKitten = new int[MAX];
		scorePerKitten = new int[MAX];
		for (int i = 0; i < MAX; i++) {
			playedPerKitten[i] = 0;
			scorePerKitten[i] = START_SCORE;
		}
	}

	public synchronized int get(int kittenId) {
		int score = scorePerKitten[kittenId];

		int ranking = 1;
		for (int otherScore : scorePerKitten) {
			if (otherScore > score) {
				ranking++;
			}
		}

		return ranking;
	}

	public synchronized void vote(int kittenIdWinner, int kittenIdLoser) {
		int score1 = scorePerKitten[kittenIdWinner];
		int score2 = scorePerKitten[kittenIdLoser];
		int d = Math.min(400, Math.abs(score1 - score2));
		float p = 1f / (1f + (float) Math.pow(10, -d / 400f));
		int k1 = k(score1, ++playedPerKitten[kittenIdWinner]);
		int k2 = k(score2, ++playedPerKitten[kittenIdLoser]);

		int r1 = Math.round(score1 + (k1 * (1 - p)));
		int r2 = Math.round(score2 + (k2 * (0 - p)));

		scorePerKitten[kittenIdWinner] = r1;
		scorePerKitten[kittenIdLoser] = r2;
	}

	private int k(int score, int played) {
		return played < 30 ? 25 : score < 2400 ? 15 : 10;
	}
}