package com.danielpuiu.swing.xml;

import com.danielpuiu.swing.component.RelativeContainer;
import com.danielpuiu.swing.type.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ComponentHandler extends DefaultHandler {

    private static final List<String> relativeLayoutMethods = Stream.of(RelativeContainer.class.getDeclaredMethods()).map(Method::getName).collect(Collectors.toList());

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final List<String> importedPackageNames = initPackageNames();

    private final HashMap<String, Component> nameToComponent = new HashMap<>();

    private Stack<Component> elements = new Stack<>();

    private List<Runnable> relativeMethods = new ArrayList<>();

    <T> T getRootElement() {
        return (T) elements.pop();
    }

    public <T> T getComponent(String name) {
        return (T) nameToComponent.get(name);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            Class<?> qClass = getClass(qName);
            Component element = (Component) qClass.newInstance();
            buildElement(element, attributes);

            String name = element.getName();
            if (Objects.nonNull(name) && !name.isEmpty()) {
                nameToComponent.put(name, element);
            }

            elements.add(element);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new SAXException(e.getMessage());
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (elements.size() == 1) {
            return;
        }

        Object element = elements.pop();
        Object parent = elements.peek();

        try {
            Method method = getMethod("add", parent.getClass(), element.getClass());
            method.invoke(parent, element);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new SAXException(e.getMessage());
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();

        relativeMethods.forEach(Runnable::run);
    }

    private void buildElement(Component element, Attributes attributes) throws NoSuchMethodException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String localName = attributes.getLocalName(i);
            String value = attributes.getValue(i);

            if (relativeLayoutMethods.contains(localName)) {
                relativeMethods.add(new RelativeLayoutMethodTranslator(this, element, localName, value));
            } else {
                invokeMethod(element, localName, value);
            }
        }
    }

    private void invokeMethod(Component element, String localName, String value) throws NoSuchMethodException {
        String methodName = "set" + capitalize(localName);
        String[] values = value.split(",");

        Predicate<Method> methodMatch = method -> method.getName().equals(methodName) && method.getGenericParameterTypes().length == values.length;
        List<Method> methods = Stream.of(element.getClass().getMethods()).filter(methodMatch).collect(Collectors.toList());

        for (Method method: methods) {
            try {
                method.invoke(element, getArguments(method.getGenericParameterTypes(), values));
                return;
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                logger.trace("", e.getMessage());
            }
        }

        logger.error("Cannot invoke [{}] with arguments [{}] on component [{}].", methodName, Arrays.toString(values), element.getName());
        throw new NoSuchMethodException(methodName);
    }

    private Object[] getArguments(Type[] types, String[] values) {
        Object[] result = new Object[values.length];

        for (int i = 0; i < values.length; i++) {
            result[i] = TypeConverter.convert(types[i].getTypeName(), values[i]);
        }

        return result;
    }

    private String capitalize(String attributeLocalName) {
        return attributeLocalName.substring(0, 1).toUpperCase() + attributeLocalName.substring(1);
    }

    private Class<?> getClass(String className) throws ClassNotFoundException {
        for (String packageName: importedPackageNames) {
            try {
                return Class.forName(packageName + className);
            } catch (ClassNotFoundException e) {
                // nothing to do
            }
        }

        throw new IllegalStateException("Cannot load class named " + className);
    }

    private Method getMethod(String methodName, Class<?> parentClass, Class<?> elementClass) throws NoSuchMethodException {
        List<String> errorMessages = new ArrayList<>();
        while (Objects.nonNull(elementClass)) {
            try {
                return parentClass.getMethod(methodName, elementClass);
            } catch (NoSuchMethodException e) {
                errorMessages.add(e.getMessage());
                elementClass = elementClass.getSuperclass();
            }
        }

        throw new NoSuchMethodException(String.join(", ", errorMessages));
    }

    private List<String> initPackageNames() {
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("java.awt.");
        list.add("javax.swing.");

        return list;
    }
}
