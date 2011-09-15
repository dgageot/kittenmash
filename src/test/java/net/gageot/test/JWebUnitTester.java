package net.gageot.test;

import static ch.qos.logback.classic.Level.*;
import static org.apache.commons.lang.StringUtils.*;
import java.lang.reflect.ParameterizedType;
import java.net.*;
import java.util.Random;
import net.gageot.kittenmash.Kittens;
import net.sourceforge.jwebunit.junit.WebTester;
import org.junit.*;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.LoggerContext;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.Service;

public abstract class JWebUnitTester<T extends Service> extends WebTester {
	private static final int TRY_COUNT = 50;
	private static final int DEFAULT_PORT = 8183;
	private static final Random RANDOM = new Random();

	private T server;

	protected JWebUnitTester() {
		((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("ROOT").setLevel(INFO);
	}

	@Before
	@SuppressWarnings("unchecked")
	public void startService() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
		Class<T> serverClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];

		for (int i = 0; i < TRY_COUNT; i++) {
			try {
				int port = getRandomPort();

				server = serverClass.getDeclaredConstructor(int.class).newInstance(port);
				server.startAndWait();

				setBaseUrl("http://localhost:" + port);
				return;
			} catch (Exception e) {
				System.err.println("Unable to bind server: " + e);
			}
		}
		throw new IllegalStateException("Unable to start server");
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

	public void assertDownloadedFileEquals(String file) {
		try {
			assertDownloadedFileEquals(new URL("file:" + file));
		} catch (MalformedURLException e) {
			throw Throwables.propagate(e);
		}
	}

	@Override
	public void setBaseUrl(String url) {
		super.setBaseUrl(removeEnd(url, "/") + "/");
	}

	static final Class<?> hackUntilInfinitestIsFixed = Kittens.class;
}
