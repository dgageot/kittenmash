package net.gageot.kittenmash;

import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import com.google.inject.*;
import net.gageot.kittenmash.util.*;
import org.apache.commons.io.*;
import org.simpleframework.http.*;
import org.simpleframework.http.core.*;
import org.simpleframework.transport.connect.*;
import org.stringtemplate.v4.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

import static java.util.Arrays.*;
import static net.gageot.kittenmash.util.Reflection.*;

public class Kittens extends AbstractService implements Container {
	private SocketConnection socketConnection;
	private final int port;
	private Scores scores = new Scores();
	private Injector injector;

	public Kittens(int port) {
		this.port = port;
		injector = Guice.createInjector();
	}

	@Override
	public void handle(Request req, Response resp) {
		List<String> path = asList(req.getPath().getSegments());
		String action = Iterables.getFirst(path, "/");

		try {
			Object controller;
			if (action.equals("kitten")) {
				controller = injector.getInstance(KittenController.class);
			} else if (action.equals("vote")) {
				controller = injector.getInstance(VoteController.class);
			} else {
				controller = injector.getInstance(IndexController.class);
			}

			invoke(controller, "render", asList(resp, path));
		} finally {
			try {
				resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class IndexController {
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

	public static class VoteController {
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

	public static class KittenController {
		public void render(Response resp, List<String> path) throws IOException {
			String kittenId = path.get(1);
			Files.copy(Paths.get("kitten", kittenId + ".jpg"), resp.getOutputStream());
		}
	}

	public static void main(String[] args) {
		new Kittens(8080).startAndWait();
	}

	@Override
	protected void doStart() {
		try {
			socketConnection = new SocketConnection(this);
			socketConnection.connect(new InetSocketAddress(port));
			notifyStarted();
		} catch (IOException e) {
			notifyFailed(e);
		}
	}

	@Override
	protected void doStop() {
		try {
			socketConnection.close();
			notifyStopped();
		} catch (IOException e) {
			notifyFailed(e);
		}
	}
}
