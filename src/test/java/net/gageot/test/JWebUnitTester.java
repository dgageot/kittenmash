package net.gageot.test;

import ch.qos.logback.classic.*;
import com.google.common.util.concurrent.*;
import net.gageot.kittenmash.*;
import org.junit.*;
import org.slf4j.*;

import java.lang.reflect.*;
import java.util.*;

import static ch.qos.logback.classic.Level.*;

public abstract class JWebUnitTester<T extends Service> extends WebTesterExtended {
	private static final int TRY_COUNT = 10;
	private static final int DEFAULT_PORT = 8183;
	private static final Random RANDOM = new Random();

	private T server;

	protected JWebUnitTester() {
		((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("ROOT").setLevel(INFO);
	}

	@Before
	public void startService() {
		for (int i = 0; i < TRY_COUNT; i++) {
			try {
				int port = getRandomPort();

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

		return serverClass.getDeclaredConstructor(int.class).newInstance(port);
	}

	@After
	public void stopService() {
		if (null != server) {
			server.stopAndWait();
		}
	}

	private static int getRandomPort() {
		synchronized (RANDOM) {
			return DEFAULT_PORT + RANDOM.nextInt(1000);
		}
	}

	static final Class<?> hackUntilInfinitestIsFixed = Kittens.class;
}
