package net.gageot.kittenmash;

import com.google.inject.*;
import net.gageot.kittenmash.util.*;
import org.apache.commons.io.*;
import org.simpleframework.http.*;
import org.stringtemplate.v4.*;

import java.io.*;
import java.util.*;

public class IndexController {
	private Scores scores;

	@Inject
	public IndexController(Scores scores) {
		this.scores = scores;
	}

	public void render(Response resp, List<String> path) throws IOException {
		String html = FileUtils.readFileToString(new File("index.html"));
		ST template = new ST(html, '$', '$');
		template.add("leftScore", scores.get(1));
		template.add("rightScore", scores.get(2));

		resp.getPrintStream().append(template.render());
	}
}
