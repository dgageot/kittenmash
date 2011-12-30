package net.gageot.kittenmash;

import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import com.google.inject.*;
import net.gageot.kittenmash.util.*;
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

public class Kittens extends AbstractService implements Container {
	private final int port;
	private Injector injector;
	private SocketConnection socketConnection;

	public Kittens(int port) {
		this.port = port;
		injector = createInjector(new GuiceModule() {
			@Override
			protected void configure() {
				bind(Object.class, named("kitten")).to(KittenController.class);
				bind(Object.class, named("vote")).to(VoteController.class);
				bind(Object.class, named("/")).to(IndexController.class);
			}
		});
	}

	@Override
	public void handle(Request req, Response resp) {
		List<String> path = asList(req.getPath().getSegments());
		String action = getFirst(path, "/");

		try {
			invoke(controller(action), "render", arguments(resp, path));
		} finally {
			try {
				resp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private List<Object> arguments(Response resp, List<String> path) {
		return ImmutableList.builder().add(resp).addAll(Iterables.skip(path, 1)).build();
	}

	private Object controller(String action) {
		return injector.getInstance(Key.get(Object.class, named(action)));
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
