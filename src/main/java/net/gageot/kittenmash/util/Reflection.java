package net.gageot.kittenmash.util;

import java.lang.reflect.Method;
import java.util.List;
import com.google.common.base.Throwables;

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
			for (Method method : target.getClass().getMethods()) {
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

				method.invoke(target, convertedArguments);
			}
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
}
