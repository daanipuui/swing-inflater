package com.danielpuiu.swing.xml;

import com.danielpuiu.swing.component.RelativeContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Stream;

public class RelativeLayoutMethodTranslator implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ComponentHandler componentHandler;

    private Component first;

    private String methodName;
    private String secondName;

    RelativeLayoutMethodTranslator(ComponentHandler componentHandler, Component first, String methodName, String secondName) {
        this.componentHandler = componentHandler;
        this.first = first;
        this.methodName = methodName;
        this.secondName = secondName;
    }

    @Override
    public void run() {
        if (!RelativeContainer.class.isInstance(first.getParent())) {
            logger.error("[{}] is not contained in a RelativeContainer.", first.getName());
            throw new IllegalStateException();
        }

        if ("false".equalsIgnoreCase(secondName)) {
            return;
        }

        if ("true".equalsIgnoreCase(secondName)) {
            invokeMethod(first);
            return;
        }

        if ("width".equalsIgnoreCase(methodName) || "height".equalsIgnoreCase(methodName)) {
            invokeMethod(first, Double.parseDouble(secondName));
            return;
        }

        Component second = componentHandler.getComponent(secondName);
        if (Objects.isNull(second)) {
            throw new IllegalArgumentException("Component name unknown: " + secondName);
        }

        invokeMethod(first, second);
    }

    private void invokeMethod(Object... objects) {
        try {
            Class[] classes = Stream.of(objects).map(object -> object instanceof Component? Component.class: object.getClass()).toArray(Class[]::new);

            Method method = RelativeContainer.class.getMethod(methodName, classes);
            method.invoke(first.getParent(), objects);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException("Cannot invoke method: " + methodName + " because " + e.getMessage());
        }
    }
}
