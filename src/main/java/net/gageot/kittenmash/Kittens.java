package net.gageot.kittenmash;

import com.google.common.util.concurrent.*;
import org.simpleframework.http.*;
import org.simpleframework.http.core.*;
import org.simpleframework.transport.connect.*;

import java.io.*;
import java.net.*;

public class Kittens extends AbstractService implements Container {
	private SocketConnection socketConnection;
	private final int port;

	public Kittens(int port) {
		this.port = port;
	}

	@Override
	public void handle(Request req, Response resp) {
		try {
			resp.getPrintStream().append("Kitten FaceMash").close();
		} catch (IOException e) {
			e.printStackTrace();
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
