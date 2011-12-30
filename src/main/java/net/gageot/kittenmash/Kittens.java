package net.gageot.kittenmash;

import com.google.common.collect.*;
import com.google.common.util.concurrent.*;
import org.apache.commons.io.*;
import org.simpleframework.http.*;
import org.simpleframework.http.core.*;
import org.simpleframework.transport.connect.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

import static java.util.Arrays.*;

public class Kittens extends AbstractService implements Container {
	private SocketConnection socketConnection;
	private final int port;

	public Kittens(int port) {
		this.port = port;
	}

	@Override
	public void handle(Request req, Response resp) {
		List<String> path = asList(req.getPath().getSegments());
		String action = Iterables.getFirst(path, "/");

		try {
			if (action.equals("kitten")) {
				String kittenId = path.get(1);
				Files.copy(Paths.get("kitten", kittenId + ".jpg"), resp.getOutputStream());
			} else if (action.equals("vote")) {
				String html = FileUtils.readFileToString(new File("index2.html"));
				resp.getPrintStream().append(html);
			} else {
				String html = FileUtils.readFileToString(new File("index.html"));
				resp.getPrintStream().append(html);
			}
		} catch (IOException e) {
			e.printStackTrace();
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
