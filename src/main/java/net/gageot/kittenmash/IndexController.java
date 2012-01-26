package net.gageot.kittenmash;

import com.google.common.base.*;
import com.google.common.io.*;
import com.google.common.util.concurrent.*;
import com.google.inject.*;
import org.simpleframework.http.*;
import org.stringtemplate.v4.*;

import java.io.*;
import java.util.*;

public class IndexController {
	private final AtomicLongMap<Integer> scores;
	private final Random random;

	@Inject
	public IndexController(AtomicLongMap<Integer> scores, Random random) {
		this.scores = scores;
		this.random = random;
	}

	public void render(Response resp) throws IOException {
		int leftKittenId = random.nextInt(10);
		int rightKittenId = random.nextInt(10);

		String html = Files.toString(new File("index.html"), Charsets.UTF_8);
		ST template = new ST(html, '$', '$');
		template.add("leftKittenId", leftKittenId);
		template.add("rightKittenId", rightKittenId);
		template.add("leftScore", scores.get(leftKittenId));
		template.add("rightScore", scores.get(rightKittenId));

		resp.getPrintStream().append(template.render());
	}
}
