package com.danielpuiu.swing.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class ComponentLoader {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ComponentHandler componentHandler = new ComponentHandler();

    public <T> T load(String filePath) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(filePath, componentHandler);

            return componentHandler.getRootElement();
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logger.error("Error", e);
        }

        return null;
    }

    public <T> T getComponent(String name) {
        return componentHandler.getComponent(name);
    }

    public void register(String... packageNames) {
        componentHandler.registerPackageName(packageNames);
    }
}
