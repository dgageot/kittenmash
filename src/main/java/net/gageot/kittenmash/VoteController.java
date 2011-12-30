package net.gageot.kittenmash;

import com.google.inject.*;
import net.gageot.kittenmash.util.*;
import org.simpleframework.http.*;

public class VoteController {
	private Scores scores;

	@Inject
	public VoteController(Scores scores) {
		this.scores = scores;
	}

	public void render(Response resp, int kittenId) {
		scores.win(kittenId);

		resp.setCode(Status.TEMPORARY_REDIRECT.getCode());
		resp.add("Location", "/");
	}
}
