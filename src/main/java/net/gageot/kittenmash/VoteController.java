package net.gageot.kittenmash;

import com.google.common.util.concurrent.*;
import com.google.inject.*;
import org.simpleframework.http.*;

public class VoteController {
	private final AtomicLongMap<Integer> scores;

	@Inject
	public VoteController(AtomicLongMap<Integer> scores) {
		this.scores = scores;
	}

	public void render(Response resp, int kittenId) {
		scores.incrementAndGet(kittenId);

		resp.setCode(Status.TEMPORARY_REDIRECT.getCode());
		resp.add("Location", "/");
	}
}
