package net.gageot.kittenmash;

import com.google.common.util.concurrent.*;
import com.google.inject.*;

import java.util.*;

import static net.gageot.kittenmash.WebServer.*;

public class KittenWebApp extends AbstractWebApp {
	@Override
	protected void configure() {
		bind(new TypeLiteral<AtomicLongMap<Integer>>() {}).toInstance(AtomicLongMap.<Integer>create());
		bind(Random.class).toInstance(new Random());

		route("kitten").to(KittenController.class);
		route("vote").to(VoteController.class);
		route("/").to(IndexController.class);
	}

	public static void main(String[] args) throws Exception {
		new WebServer(8080, new KittenWebApp()).startAndWait();
	}
}
