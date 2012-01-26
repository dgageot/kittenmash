package net.gageot.kittenmash;

import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import com.google.inject.*;

import java.io.*;
import java.util.*;

import static net.gageot.kittenmash.WebServer.*;

public class IndexController {
	private final AtomicLongMap<Integer> scores;
	private final Random random;

	@Inject
	public IndexController(AtomicLongMap<Integer> scores, Random random) {
		this.scores = scores;
		this.random = random;
	}

	public void render(Answer answer) throws IOException {
		int leftId = random.nextInt(10);
		int rightId = random.nextInt(10);

		answer.serve(new File("index.html"), ImmutableMap.of( //
				"leftKittenId", leftId, //
				"rightKittenId", rightId, //
				"leftScore", scores.get(leftId), //
				"rightScore", scores.get(rightId)));
	}
}
