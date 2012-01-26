package net.gageot.test;

import com.google.common.util.concurrent.*;
import com.google.inject.*;
import org.junit.*;

import java.lang.reflect.*;
import java.util.*;

public abstract class JWebUnitTester<T extends Service> extends WebTesterExtended {
	private static final int TRY_COUNT = 10;
	private static final int DEFAULT_PORT = 8183;

	private T server;

	protected abstract Module useModule();

	@Before
	public void startService() {
		for (int i = 0; i < TRY_COUNT; i++) {
			int port = DEFAULT_PORT + new Random().nextInt(1000);

			try {
				server = createServer(port);
				server.startAndWait();

				setBaseUrl("http://localhost:" + port);
				return;
			} catch (Exception e) {
				System.err.println("Unable to bind server: " + e);
			}
		}
		throw new IllegalStateException("Unable to start server");
	}

	@SuppressWarnings("unchecked")
	private T createServer(int port) throws Exception {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		Class<T> serverClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];

		return serverClass.getDeclaredConstructor(int.class, Module.class).newInstance(port, useModule());
	}

	@After
	public void stopService() {
		if (null != server) {
			server.stopAndWait();
		}
	}
}
