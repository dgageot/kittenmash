package net.gageot.kittenmash.util;

import javax.inject.Singleton;

@Singleton
public class Scores {
	private final int[] scores = new int[11];

	public int get(int kittenId) {
		return scores[kittenId];
	}

	public void win(int kittenId) {
		scores[kittenId]++;
	}
}
