package net.gageot.kittenmash;

import org.simpleframework.http.*;
import org.simpleframework.http.core.*;
import org.simpleframework.transport.connect.*;

import java.io.*;
import java.net.*;

public class Kittens {
	public static void main(String[] args) throws Exception {
		new SocketConnection(new Container() {
			@Override
			public void handle(Request req, Response resp) {
				try {
					resp.getPrintStream().append("Kitten FaceMash").close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).connect(new InetSocketAddress(8080));
	}
}
