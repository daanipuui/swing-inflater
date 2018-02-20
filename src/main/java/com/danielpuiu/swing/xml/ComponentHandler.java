package com.danielpuiu.swing.xml;

import com.danielpuiu.swing.type.TypeConversion;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ComponentHandler extends DefaultHandler implements TypeConversion {

    private static final String VALUE_DELIMITER = ",";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Set<String> importedPackageNames = initPackageNames();

    private final HashMap<String, Component> nameToComponent = new HashMap<>();

    private Stack<Component> elements = new Stack<>();

    private List<Runnable> futures = new ArrayList<>();

    <T> T getRootElement() {
        return (T) elements.pop();
    }

    public <T> T getComponent(String name) {
        return (T) nameToComponent.get(name);
    }

    ComponentHandler() {
        TypeConverter.registerConverter(this);
    }

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Component.class.getName(), Component.class.getSimpleName(), "component");
    }

    @Override
    public Object convert(String value) {
        return getComponent(value);
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
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
            // TODO
            Method method = getMethod("add", parent.getClass(), element.getClass());
            method.invoke(parent, element);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new SAXException(e.getMessage());
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();

        futures.forEach(Runnable::run);
    }

    private void buildElement(Component element, Attributes attributes) {
        for (int i = 0; i < attributes.getLength(); i++) {
            String methodName = attributes.getLocalName(i);
            String[] arguments = attributes.getValue(i).split(VALUE_DELIMITER);

            List<Method> methods = getCandidateMethods(element, methodName, arguments.length);
            if (!methods.isEmpty()) {
                invokeMethod(element, methods, arguments);
                continue;
            }

            futures.add(() -> invokeParentMethod(element, methodName, arguments));
        }
    }

    private void invokeParentMethod(Component element, String methodName, String[] arguments) {
        Component parent = element.getParent();
        List<Method> methods = getCandidateMethods(parent, methodName, arguments.length + 1);
        invokeMethod(parent, methods, Stream.concat(Stream.of(element.getName()), Stream.of(arguments)).toArray(String[]::new));
    }

    private List<Method> getCandidateMethods(Component element, String methodName, int argumentsCount) {
        List<String> methodNames = Arrays.asList(methodName, "set" + capitalize(methodName));
        List<Class<?>> returnTypes = Arrays.asList(void.class, boolean.class, Boolean.class);

        return Stream.of(element.getClass().getMethods())
                .filter(method -> methodNames.contains(method.getName()))
                .filter(method -> method.getParameterCount() == argumentsCount)
                .filter(method -> returnTypes.contains(method.getReturnType()))
                .collect(Collectors.toList());
    }

    private void invokeMethod(Component element, List<Method> candidateMethods, String[] arguments) {
        for (Method method: candidateMethods) {
            try {
                method.invoke(element, getArguments(method.getGenericParameterTypes(), arguments));
                logger.info("Invoked method [{}] on [{}] with parameters [{}].", method.getName(), element.getName(), Arrays.toString(arguments));
                return;
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                logger.debug("Cannot invoke method [{}] on [{}] with parameters [{}] because [{}]", method.getName(), element.getName(), Arrays.toString(arguments), e.getCause());
            }
        }
    }

    private Object[] getArguments(Type[] types, String[] values) {
        Object[] convertedValues = new Object[values.length];
        for (int i = 0; i < values.length; i++) {
            convertedValues[i] = TypeConverter.convert(types[i].getTypeName(), values[i]);
        }

        return convertedValues;
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

    void registerPackageName(String... packageNames) {
        importedPackageNames.addAll(Arrays.asList(packageNames));
    }

    private Set<String> initPackageNames() {
        Set<String> set = new HashSet<>();
        set.add("");
        set.add("java.awt.");
        set.add("javax.swing.");

        return set;
    }
}
