package com.danielpuiu.swing.xml;

import com.danielpuiu.swing.constraints.ConstraintsConverter;
import com.danielpuiu.swing.type.TypeConversion;
import com.danielpuiu.swing.type.TypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.danielpuiu.swing.util.ObjectUtil.cast;

class ComponentHandler extends DefaultHandler implements TypeConversion {

    private static final String Q_NAME_DELIMITER = ":";
    private static final String VALUE_DELIMITER = ",";

    private static final String EMPTY_PREFIX = "";
    private static final String LAYOUT_PREFIX = "layout";

    private static final String SETTER_PREFIX = "set";
    private static final String PACKAGE_DELIMITER = ".";

    private static final int FIRST_CHARACTER = 0;
    private static final int SECOND_CHARACTER = 1;

    private static final int PREFIX = 0;
    private static final int LOCAL_NAME = 1;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Set<String> importedPackageNames = initPackageNames();

    private final Map<Component, Map<String, Map<String, String>>> elementAttributes = new HashMap<>();

    private final Map<Component, List<Component>> parentToComponents = new HashMap<>();

    private final Map<String, Component> nameToComponent = new HashMap<>();

    private Stack<Component> elements = new Stack<>();

    private Container parent;

    ComponentHandler() {
        TypeConverter.registerConverter(this);
    }

    <T> T getRoot() {
        return cast(parent);
    }

    <T extends Component> T getComponent(String name) {
        return cast(nameToComponent.get(name));
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            Class<Component> qClass = getClass(qName);
            Component element = qClass.newInstance();
            elementAttributes.put(element, getAttributesByPrefix(attributes));

            elements.add(element);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SAXException(e.getMessage());
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (elements.size() == 1) {
            return;
        }

        Component element = elements.pop();
        Container parent = (Container) elements.peek();

        parentToComponents.computeIfAbsent(parent, key -> new ArrayList<>()).add(element);
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();

        try {
            parent = (Container) elements.pop();
            buildComponent(parent);
            buildLayout(parent);
        } catch (IllegalArgumentException e) {
            throw new SAXException(e.getMessage());
        } finally {
            TypeConverter.unregisterConverter(this);
        }
    }

    @Override
    public List<String> getHandledTypes() {
        return Arrays.asList(Component.class.getName(), Component.class.getSimpleName(), "component");
    }

    @Override
    public Object convert(String value) {
        return getComponent(value);
    }

    private Map<String, Map<String, String>> getAttributesByPrefix(Attributes attributes) {
        Map<String, Map<String, String>> attributesByPrefix = new HashMap<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            String qName = attributes.getQName(i);
            if (!qName.contains(Q_NAME_DELIMITER)) {
                qName = Q_NAME_DELIMITER + qName;
            }

            String[] parts = qName.split(Q_NAME_DELIMITER);
            attributesByPrefix.computeIfAbsent(parts[PREFIX], key -> new HashMap<>()).put(parts[LOCAL_NAME], attributes.getValue(i));
        }

        return attributesByPrefix;
    }

    private void buildComponent(Component component) {
        Map<String, Map<String, String>> attributesByPrefix = elementAttributes.getOrDefault(component, Collections.emptyMap());
        callMethods(component, attributesByPrefix.getOrDefault(EMPTY_PREFIX, Collections.emptyMap()));

        registerComponentName(component);
        parentToComponents.getOrDefault(component, Collections.emptyList()).forEach(this::buildComponent);
    }

    private void registerComponentName(Component component) {
        String name = component.getName();
        if (Objects.nonNull(name)) {
            nameToComponent.put(name, component);
        }
    }

    private void callMethods(Object object, Map<String, String> attributes) {
        for (Map.Entry<String, String> entry: attributes.entrySet()) {
            String methodName = getMethodName(entry.getKey());
            String[] arguments = entry.getValue().split(VALUE_DELIMITER);

            List<Method> methods = getCandidateMethods(object, methodName, arguments.length);
            if (!methods.isEmpty()) {
                invokeMethod(object, methods, arguments);
                continue;
            }

            throw new IllegalArgumentException(String.format("Method [%s] not found.", methodName));
        }
    }

    private void buildLayout(Container container) {
        parentToComponents.getOrDefault(container, Collections.emptyList())
                .stream()
                .filter(Container.class::isInstance)
                .forEach(child -> addComponentToLayout(container, child));

        LayoutManager layout = container.getLayout();
        if (Objects.nonNull(layout)) {
            Map<String, Map<String, String>> attributesByPrefix = elementAttributes.getOrDefault(container, Collections.emptyMap());
            callMethods(layout, attributesByPrefix.getOrDefault(LAYOUT_PREFIX, Collections.emptyMap()));
        }
    }

    private void addComponentToLayout(Container parent, Component component) {
        LayoutManager layout = parent.getLayout();
        if (Objects.nonNull(layout)) {
            Object constraints = getConstraints(layout.getClass().getName(), component);
            parent.add(component, constraints);
        }

        if (component instanceof Container) {
            buildLayout((Container) component);
        }
    }

    private Object getConstraints(String layout, Component component) {
        Map<String, Map<String, String>> attributesByPrefix = elementAttributes.getOrDefault(component, Collections.emptyMap());
        Map<String, String> layoutAttributes = attributesByPrefix.remove(LAYOUT_PREFIX);
        return ConstraintsConverter.convert(layout, Optional.ofNullable(layoutAttributes).orElse(Collections.emptyMap()));
    }

    private List<Method> getCandidateMethods(Object object, String methodName, int argumentsCount) {
        List<Class<?>> returnTypes = Arrays.asList(void.class, boolean.class, Boolean.class);

        return Stream.of(object.getClass().getMethods())
                .filter(method -> methodName.equals(method.getName()))
                .filter(method -> method.getParameterCount() == argumentsCount)
                .filter(method -> returnTypes.contains(method.getReturnType()))
                .collect(Collectors.toList());
    }

    private void invokeMethod(Object object, List<Method> candidateMethods, String[] arguments) {
        for (Method method: candidateMethods) {
            try {
                method.invoke(object, getArguments(method.getGenericParameterTypes(), arguments));
                logger.info("Invoked method [{}] on [{}] with parameters {}.", method.getName(), object, Arrays.toString(arguments));
                return;
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                logger.debug("Cannot invoke method [{}] on [{}] with parameters {} because {}", method.getName(), object, Arrays.toString(arguments), e);
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

    private String getMethodName(String name) {
        return SETTER_PREFIX + name.substring(FIRST_CHARACTER, SECOND_CHARACTER).toUpperCase() + name.substring(SECOND_CHARACTER);
    }

    private Class<Component> getClass(String className) {
        for (String packageName: importedPackageNames) {
            try {
                return cast(Class.forName(packageName + className));
            } catch (ClassNotFoundException e) {
                // nothing to do
            }
        }

        throw new IllegalStateException(String.format("Cannot load class [%s].", className));
    }

    void registerPackageName(String... packageNames) {
        Set<String> packages = Stream.of(packageNames)
                .map(packageName -> packageName.endsWith(PACKAGE_DELIMITER)? packageName: packageName + PACKAGE_DELIMITER)
                .collect(Collectors.toSet());

        importedPackageNames.addAll(packages);
    }

    private Set<String> initPackageNames() {
        Set<String> set = new HashSet<>();
        set.add("");
        set.add("java.awt.");
        set.add("javax.swing.");

        return set;
    }
}
