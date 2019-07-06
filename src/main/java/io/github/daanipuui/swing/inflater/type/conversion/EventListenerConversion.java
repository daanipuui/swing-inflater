package io.github.daanipuui.swing.inflater.type.conversion;

import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.type.TypeConversion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.daanipuui.swing.inflater.util.ObjectUtil.cast;

public class EventListenerConversion implements TypeConversion<EventListener> {

    private static final String METHOD_REFERENCE_DELIMITER = "::";

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(EventListener.class);
    }

    @Override
    public EventListener convertLiteral(PackageProvider packageProvider, String value) {
        String[] values = value.split(METHOD_REFERENCE_DELIMITER);
        if (values.length != 2) {
            String errorMessage = String.format("[%s] is not a valid method reference.", value);
            throw new IllegalArgumentException(errorMessage);
        }

        String type = values[0].trim();
        Class cls = packageProvider.getClass(type);
        if (cls == null) {
            String errorMessage = String.format("Unknown class name [%s]. Register the package or use full qualified name.", type);
            throw new IllegalArgumentException(errorMessage);
        }

        Method method = getMethod(cls, values[1].trim());
        return invokeMethod(method);
    }

    private EventListener invokeMethod(Method method) {
        try {
            return cast(method.invoke(null));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Method getMethod(Class cls, String methodName) {
        List<Method> methods = Arrays.stream(cls.getMethods())
                .filter(method -> method.getName().equals(methodName))
                .filter(method -> Modifier.isStatic(method.getModifiers()))
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .filter(method -> !Modifier.isAbstract(method.getModifiers()))
                .filter(method -> method.getParameterCount() == 0)
                .filter(method -> EventListener.class.isAssignableFrom(method.getReturnType()))
                .collect(Collectors.toList());

        if (methods.isEmpty()) {
            String errorMessage = String.format("Public static method [%s] with no parameters not found.", methodName);
            throw new IllegalArgumentException(errorMessage);
        }

        return methods.get(0);
    }
}
