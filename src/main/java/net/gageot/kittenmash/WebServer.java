package net.gageot.kittenmash;

import com.google.common.collect.*;
import com.google.common.io.*;
import com.google.common.util.concurrent.*;
import com.google.inject.*;
import com.google.inject.binder.*;
import lombok.*;
import net.gageot.kittenmash.util.*;
import org.simpleframework.http.*;
import org.simpleframework.http.core.*;
import org.simpleframework.transport.connect.*;
import org.stringtemplate.v4.*;

import java.io.*;
import java.net.*;
import java.util.*;

import static com.google.common.base.Charsets.*;
import static com.google.common.collect.Iterables.*;
import static com.google.inject.Guice.*;
import static com.google.inject.Key.get;
import static com.google.inject.name.Names.*;
import static java.util.Arrays.*;
import static org.simpleframework.http.Status.*;

public class WebServer extends AbstractIdleService implements Container {
	private final int port;
	private final Injector injector;
	private SocketConnection socketConnection;

	public WebServer(int port, Module module) {
		this.port = port;
		injector = createInjector(module);
	}

	@Override
	public void handle(Request req, Response resp) {
		List<String> path = asList(req.getPath().getSegments());
		String action = getFirst(path, "/");

		try {
			Reflection.invoke(controller(action), "render", arguments(resp, path));
		} finally {
			try {
				resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Object controller(String action) {
		return injector.getInstance(get(Object.class, named(action)));
	}

	private static List<Object> arguments(Response resp, List<String> path) {
		return ImmutableList.builder().add(new Answer(resp)).addAll(skip(path, 1)).build();
	}

	@Override
	protected void startUp() throws IOException {
		socketConnection = new SocketConnection(this);
		socketConnection.connect(new InetSocketAddress(port));
	}

	@Override
	protected void shutDown() throws IOException {
		socketConnection.close();
	}

	public static abstract class AbstractWebApp extends AbstractModule {
		protected LinkedBindingBuilder<Object> route(String action) {
			return bind(Key.get(Object.class, named(action)));
		}
	}

	public static class Answer {
		@Delegate
		private final Response response;

		Answer(Response response) {
			this.response = response;
		}

		public void redirect(String url) {
			response.setCode(TEMPORARY_REDIRECT.getCode());
			response.add("Location", url);
		}

		public void serve(File file) throws IOException {
			Files.copy(file, response.getOutputStream());
		}

		public void serve(File file, Map<String, ?> model) throws IOException {
			String html = Files.toString(file, UTF_8);

			ST template = new ST(html, '$', '$');
			for (Map.Entry<String, ?> entry : model.entrySet()) {
				template.add(entry.getKey(), entry.getValue());
			}

			response.getPrintStream().append(template.render());
		}
	}
}
