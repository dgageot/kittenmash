package net.gageot.kittenmash;

import com.google.inject.*;
import com.google.inject.binder.*;

import static com.google.inject.name.Names.named;

abstract class Routes extends AbstractModule {
	protected LinkedBindingBuilder<Object> route(String action) {
		return bind(Key.get(Object.class, named(action)));
	}
}
