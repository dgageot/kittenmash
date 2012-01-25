package net.gageot.kittenmash.util;

import com.google.common.base.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Should be into src/main or a dependency jar. I put it here to make the
 * demonstration easier to follow.
 */
public class Reflection {
	private Reflection() {
		// Static utility class
	}

	public static void invoke(Object target, String methodName, List<Object> arguments) {
		try {
			for (Method method : target.getClass().getDeclaredMethods()) {
				if (!method.getName().equals(methodName)) {
					continue;
				}

				Object[] convertedArguments = new Object[arguments.size()];

				int i = 0;
				for (Object argument : arguments) {
					Object convertedArgument = argument;
					if (method.getParameterTypes()[i] == int.class) {
						convertedArgument = Integer.parseInt((String) argument);
					}
					convertedArguments[i++] = convertedArgument;
				}

				method.setAccessible(true);
				method.invoke(target, convertedArguments);
			}
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
}
