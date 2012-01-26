package net.gageot.kittenmash;

import com.google.common.util.concurrent.*;
import com.google.inject.*;

import static net.gageot.kittenmash.WebServer.*;

public class VoteController {
	private final AtomicLongMap<Integer> scores;

	@Inject
	public VoteController(AtomicLongMap<Integer> scores) {
		this.scores = scores;
	}

	public void render(Answer answer, int kittenId) {
		scores.incrementAndGet(kittenId);

		answer.redirect("/");
	}
}
