package com.danielpuiu.swing.inflater.xml;

import com.danielpuiu.swing.inflater.ContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.swing.JComponent;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComponentLoader implements ContextProvider {

    private static final String PACKAGE_DELIMITER = ".";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ComponentHandler componentHandler = new ComponentHandler(this);

    private final Set<String> importedPackageNames = initPackageNames();

    private final Map<String, Class<? extends JComponent>> aliases = new HashMap<>();

    public <T> T load(InputStream inputStream) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(inputStream, componentHandler);

            return componentHandler.getRoot();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.error("Error", e);
        }

        return null;
    }

    public <T extends JComponent> T getComponent(String name) {
        return componentHandler.getComponent(name);
    }

    public void register(String... packageNames) {
        Set<String> packages = Stream.of(packageNames).map(packageName ->
                packageName.endsWith(PACKAGE_DELIMITER) ? packageName : (packageName + PACKAGE_DELIMITER)).collect(Collectors.toSet());

        importedPackageNames.addAll(packages);
    }

    public Set<String> getPackageNames() {
        return new HashSet<>(importedPackageNames);
    }

    public void register(String alias, Class<? extends JComponent> cls) {
        aliases.put(alias, cls);
    }

    public Class<? extends JComponent> getAliasClass(String alias) {
        return aliases.get(alias);
    }

    private Set<String> initPackageNames() {
        Set<String> set = new HashSet<>();
        set.add("");
        set.add("java.awt.");
        set.add("javax.swing.");

        return set;
    }
}
