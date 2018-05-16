package com.gromoks.container.reader.xml;

import com.gromoks.container.entity.BeanDefinition;
import com.gromoks.container.reader.BeanDefinitionReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLBeanDefinitionReader extends DefaultHandler implements BeanDefinitionReader {
    private static final String BEAN_ELEMENT = "bean";
    private static final String PROPERTY_ELEMENT = "property";
    private static final String ID_ATTRIBUTE = "id";
    private static final String BEAN_CLASS_NAME_ATTRIBUTE = "class";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String VALUE_ATTRIBUTE = "value";
    private static final String REF_ATTRIBUTE = "ref";

    private List<BeanDefinition> beanDefinitionList;
    private String[] pathList;

    public XMLBeanDefinitionReader(String[] pathList) {
        this.pathList = pathList;
        beanDefinitionList = new ArrayList<>();
    }

    @Override
    public List<BeanDefinition> readBeanDefinition() {
        parseDocuments();
        return beanDefinitionList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase(BEAN_ELEMENT)) {
            String id = attributes.getValue(ID_ATTRIBUTE);
            String clazz = attributes.getValue(BEAN_CLASS_NAME_ATTRIBUTE);

            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setId(id);
            beanDefinition.setBeanClassName(clazz);
            beanDefinitionList.add(beanDefinition);

        } else if (qName.equalsIgnoreCase(PROPERTY_ELEMENT)) {
            BeanDefinition beanDefinition = beanDefinitionList.get(beanDefinitionList.size() - 1);
            Map<String, String> dependencies = beanDefinition.getDependencies();
            Map<String, String> refDependencies = beanDefinition.getRefDependencies();
            String value = attributes.getValue(VALUE_ATTRIBUTE);
            String ref = attributes.getValue(REF_ATTRIBUTE);
            if (value != null) {
                if (dependencies == null) {
                    dependencies = new HashMap<>();
                }
                dependencies.put(attributes.getValue(NAME_ATTRIBUTE), value);
                beanDefinition.setDependencies(dependencies);
            } else if (ref != null) {
                if (refDependencies == null) {
                    refDependencies = new HashMap<>();
                }
                refDependencies.put(attributes.getValue(NAME_ATTRIBUTE), ref);
                beanDefinition.setRefDependencies(refDependencies);
            }
        }
    }

    private void parseDocuments() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            for (String path : pathList) {
                parser.parse(path, this);
            }
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("ParserConfig error", e);
        } catch (SAXException e) {
            throw new RuntimeException("SAXException : xml not well formed", e);
        } catch (IOException e) {
            throw new RuntimeException("IO error", e);
        }
    }
}
