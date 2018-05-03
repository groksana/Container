package com.gromoks.container.reader.impl;

import com.gromoks.container.entity.BeanDefinition;
import com.gromoks.container.reader.BeanDefinitionReader;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class XMLBeanDefinitionReader implements BeanDefinitionReader {
    private static final String BEAN_NODE_EXPRESSION = "/beans/bean";
    private static final String PROPERTIES_EXPRESSION = "property";
    private static final String ID_ATTRIBUTE = "id";
    private static final String BEAN_CLASS_NAME_ATTRIBUTE = "class";
    private static final String NAME_PROPERTY = "name";
    private static final String VALUE_PROPERTY = "value";
    private static final String REF_PROPERTY = "ref";

    private final File file;

    public XMLBeanDefinitionReader(File file) {
        this.file = file;
    }

    public List<BeanDefinition> readBeanDefinition() {
        List<BeanDefinition> beanDefinitionList = new ArrayList<>();
        NodeList beanNodeList = getNodeList(BEAN_NODE_EXPRESSION);

        for (int i = 0; i < beanNodeList.getLength(); i++) {
            BeanDefinition beanDefinition = new BeanDefinition();
            NamedNodeMap attributeMap = beanNodeList.item(i).getAttributes();
            setAttributes(beanDefinition, attributeMap);

            NodeList childrenList = beanNodeList.item(i).getChildNodes();
            setChildren(beanDefinition, childrenList);
            beanDefinitionList.add(beanDefinition);
        }

        return beanDefinitionList;
    }

    private NodeList getNodeList(String expression) {
        NodeList nodeList = null;

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            Document xmlDocument = builder.parse(fileInputStream);

            XPath xPath = XPathFactory.newInstance().newXPath();

            nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);

        } catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
            e.printStackTrace();
        }
        return nodeList;
    }

    private void setAttributes(BeanDefinition beanDefinition, NamedNodeMap attributeMap) {
        for (int i = 0; i < attributeMap.getLength(); i++) {
            String attributeName = attributeMap.item(i).getNodeName();
            String attributeValue = attributeMap.item(i).getNodeValue();

            switch (attributeName) {
                case ID_ATTRIBUTE:
                    beanDefinition.setId(attributeValue);
                    break;
                case BEAN_CLASS_NAME_ATTRIBUTE:
                    beanDefinition.setBeanClassName(attributeValue);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid attribute name");
            }
        }
    }

    private void setChildren(BeanDefinition beanDefinition, NodeList childrenList) {
        Map<String, String> dependencies = null;
        Map<String, String> refDependencies = null;

        for (int i = 0; i < childrenList.getLength(); i++) {
            if (PROPERTIES_EXPRESSION.equals(childrenList.item(i).getNodeName())) {
                NamedNodeMap childAttributes = childrenList.item(i).getAttributes();
                String name = null;
                String value = null;
                String ref = null;

                for (int j = 0; j < childAttributes.getLength(); j++) {
                    String attributeName = childAttributes.item(j).getNodeName();
                    String attributeValue = childAttributes.item(j).getNodeValue();

                    switch (attributeName) {
                        case NAME_PROPERTY:
                            name = attributeValue;
                            break;
                        case VALUE_PROPERTY:
                            value = attributeValue;
                            break;
                        case REF_PROPERTY:
                            ref = attributeValue;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid attribute name");
                    }
                }

                if (value != null) {
                    if (dependencies == null) {
                        dependencies = new ConcurrentHashMap<>();
                    }
                    dependencies.put(name, value);
                } else if (ref != null) {
                    if (refDependencies == null) {
                        refDependencies = new ConcurrentHashMap<>();
                    }
                    refDependencies.put(name, ref);
                }

                beanDefinition.setDependencies(dependencies);
                beanDefinition.setRefDependencies(refDependencies);
            }
        }
    }
}