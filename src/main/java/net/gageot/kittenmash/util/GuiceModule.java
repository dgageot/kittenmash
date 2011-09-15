package net.gageot.kittenmash.util;

import com.google.inject.*;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.name.Named;

public abstract class GuiceModule extends AbstractModule {
	protected <T> LinkedBindingBuilder<T> bind(Class<T> key, Named name) {
		return bind(Key.get(key, name));
	}
}
