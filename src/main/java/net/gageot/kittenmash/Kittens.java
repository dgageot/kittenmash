package net.gageot.kittenmash;

import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import com.google.inject.*;
import com.google.inject.util.*;
import org.simpleframework.http.*;
import org.simpleframework.http.core.*;
import org.simpleframework.transport.connect.*;

import java.io.*;
import java.net.*;
import java.util.*;

import static com.google.common.collect.Iterables.*;
import static com.google.inject.Guice.*;
import static com.google.inject.name.Names.*;
import static java.util.Arrays.*;
import static net.gageot.kittenmash.util.Reflection.*;

public class Kittens extends AbstractIdleService implements Container {
	private final int port;
	private final Injector injector;
	private SocketConnection socketConnection;

	public Kittens(int port, Module... overrideModules) {
		this.port = port;
		injector = createInjector(Modules.override(new Routes() {
			@Override
			protected void configure() {
				bind(new TypeLiteral<AtomicLongMap<Integer>>() {}).toInstance(AtomicLongMap.<Integer>create());
				bind(Random.class).toInstance(new Random());

				route("kitten").to(KittenController.class);
				route("vote").to(VoteController.class);
				route("/").to(IndexController.class);
			}
		}).with(overrideModules));
	}

	@Override
	public void handle(Request req, Response resp) {
		try {
			List<String> path = asList(req.getPath().getSegments());
			String action = getFirst(path, "/");

			invoke(controller(action), "render", arguments(resp, path));
		} finally {
			try {
				resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Object controller(String action) {
		return injector.getInstance(Key.get(Object.class, named(action)));
	}

	private static List<Object> arguments(Response resp, List<String> path) {
		return ImmutableList.builder().add(resp).addAll(skip(path, 1)).build();
	}

	public static void main(String[] args) {
		new Kittens(8080).startAndWait();
	}

	@Override
	protected void startUp() throws Exception {
		socketConnection = new SocketConnection(this);
		socketConnection.connect(new InetSocketAddress(port));
	}

	@Override
	protected void shutDown() throws Exception {
		socketConnection.close();
	}
}
