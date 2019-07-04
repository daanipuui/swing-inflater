package io.github.daanipuui.swing.inflater.xml;

import io.github.daanipuui.swing.inflater.ContextProvider;
import io.github.daanipuui.swing.inflater.PackageProvider;
import io.github.daanipuui.swing.inflater.constraints.ConstraintsConverter;
import io.github.daanipuui.swing.inflater.type.TypeConversion;
import io.github.daanipuui.swing.inflater.type.TypeConverter;
import io.github.daanipuui.swing.inflater.util.StringUtil;
import io.github.daanipuui.swing.inflater.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.JComponent;
import java.awt.Component;
import java.awt.LayoutManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javafx.scene.control.IndexRange.VALUE_DELIMITER;

class ComponentHandler extends DefaultHandler implements TypeConversion<Component> {

    private static final String Q_NAME_DELIMITER = ":";
    private static final String EMPTY_PREFIX = "";
    private static final String LAYOUT_CONSTRAINT_PREFIX = "layout";

    private static final String SETTER_PREFIX = "set";
    private static final String LISTENER_PREFIX = "add";

    private static final int PREFIX = 0;
    private static final int LOCAL_NAME = 1;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ContextProvider contextProvider;

    private final Map<String, JComponent> nameToComponent = new HashMap<>();

    private Deque<Object> elements = new ArrayDeque<>();

    private List<Runnable> layoutActions = new ArrayList<>();

    private JComponent root;

    ComponentHandler(ComponentLoader contextProvider) {
        this.contextProvider = contextProvider;
        TypeConverter.registerConverter(this);
    }

    <T> T getRoot() {
        return ObjectUtil.cast(root);
    }

    <T extends JComponent> T getComponent(String name) {
        return ObjectUtil.cast(nameToComponent.get(name));
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Object object = createObjectFor(qName);

        Map<String, Map<String, String>> attributesByPrefix = getAttributesByPrefix(attributes);
        buildObject(object, attributesByPrefix.remove(EMPTY_PREFIX));

        JComponent parent = ObjectUtil.cast(elements.peekLast());
        elements.add(object);

        if (object instanceof LayoutManager) {
            parent.setLayout(ObjectUtil.cast(object));
            logger.debug("Set layout [{}] on element [{}].", object.getClass().getSimpleName(), parent);
            return;
        }

        JComponent element = ObjectUtil.cast(object);
        if (Objects.isNull(parent)) {
            root = element;
            return;
        }

        layoutActions.add(() -> addLayoutComponent(parent, element, attributesByPrefix.remove(LAYOUT_CONSTRAINT_PREFIX)));
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        elements.removeLast();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();

        layoutActions.forEach(Runnable::run);
    }

    @Override
    public List<Class> getHandledTypes() {
        return Collections.singletonList(Component.class);
    }

    @Override
    public Component convertLiteral(PackageProvider packageProvider, String value) {
        return getComponent(value);
    }

    private Class<?> getClass(String className) {
        Class<? extends JComponent> cls = contextProvider.getAliasClass(className);
        if (Objects.nonNull(cls)) {
            return cls;
        }

        for (String packageName: contextProvider.getPackageNames()) {
            try {
                return ObjectUtil.cast(Class.forName(packageName + className));
            } catch (ClassNotFoundException e) {
                // nothing to do
            }
        }

        throw new IllegalStateException(String.format("Cannot load class [%s].", className));
    }

    private Object createObjectFor(String qName) throws SAXException {
        try {
            Class<?> elementClass = getClass(qName);
            return elementClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SAXException(e.getMessage());
        }
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

    private void buildObject(Object object, Map<String, String> properties) {
        if (Objects.isNull(properties)) {
            return;
        }

        for (Map.Entry<String, String> entry: properties.entrySet()) {
            String methodName = getMethodName(entry.getKey());
            if (callCandidateMethods(object, methodName, new String[]{entry.getValue()}) || callCandidateMethods(object, methodName, entry.getValue().split(VALUE_DELIMITER))) {
                continue;
            }

            throw new IllegalArgumentException(String.format("Method [%s] not found.", methodName));
        }

        if (object instanceof JComponent) {
            JComponent element = ObjectUtil.cast(object);
            String name = element.getName();
            if (nameToComponent.containsKey(name)) {
                logger.warn("Duplicate element name [{}] found. Using toString instead.", name);
                name = element.toString();
            }

            nameToComponent.put(name, element);
        }
    }

    private String getMethodName(String name) {
        String prefix = name.toLowerCase().endsWith("listener")? LISTENER_PREFIX: SETTER_PREFIX;
        return prefix + StringUtil.capitalize(name);
    }

    private boolean callCandidateMethods(Object object, String methodName, String[] arguments) {
        List<Method> methods = getCandidateMethods(object, methodName, arguments.length);
        if (methods.isEmpty()) {
            return false;
        }

        invokeMethod(object, methods, arguments);
        return true;
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
                method.invoke(object, TypeConverter.convertValues(contextProvider, method.getParameterTypes(), arguments));
                if (logger.isDebugEnabled()) {
                    logger.debug("Invoked method [{}] on [{}] with parameters {}.", method.getName(), object, Arrays.toString(arguments));
                }

                return;
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                logger.debug("Cannot invoke method [{}] on [{}] with parameters {} because {}", method.getName(), object, Arrays.toString(arguments), e);
            }
        }
    }

    private void addLayoutComponent(JComponent parent, JComponent element, Map<String, String> layoutConstraints) {
        LayoutManager layoutManager = parent.getLayout();
        if (Objects.isNull(layoutManager)) {
            parent.add(element);
            logger.debug("Add element [{}] to container [{}].", element, parent);
            return;
        }

        Object constraints = getConstraints(layoutManager, layoutConstraints);
        parent.add(element, constraints);
        logger.debug("Add element [{}] to container [{}] using constraints [{}].", element, parent, constraints);
    }

    private Object getConstraints(LayoutManager layoutManager, Map<String, String> layoutConstraints) {
        if (Objects.isNull(layoutConstraints) || layoutConstraints.isEmpty()) {
            return null;
        }

        return ConstraintsConverter.convert(contextProvider, layoutManager, layoutConstraints);
    }
}
