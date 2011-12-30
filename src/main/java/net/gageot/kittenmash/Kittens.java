package net.gageot.kittenmash;

import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import com.google.inject.*;
import org.simpleframework.http.*;
import org.simpleframework.http.core.*;
import org.simpleframework.transport.connect.*;

import java.io.*;
import java.net.*;
import java.util.*;

import static java.util.Arrays.*;
import static net.gageot.kittenmash.util.Reflection.*;

public class Kittens extends AbstractService implements Container {
	private final int port;
	private Injector injector;
	private SocketConnection socketConnection;

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
