package net.gageot.kittenmash;

import com.google.inject.*;
import net.gageot.kittenmash.util.*;
import org.simpleframework.http.*;

import java.util.*;

public class VoteController {
	private Scores scores;

	@Inject
	public VoteController(Scores scores) {
		this.scores = scores;
	}

	public void render(Response resp, List<String> path) {
		Integer kittenId = Integer.parseInt(path.get(1));
		scores.win(kittenId);

		resp.setCode(Status.TEMPORARY_REDIRECT.getCode());
		resp.add("Location", "/");
	}
}
