package com.gromoks.container.reader.impl;

import com.gromoks.container.entity.BeanDefinition;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class XMLBeanDefinitionReaderTest {
    private List<BeanDefinition> beanDefinitionList = new ArrayList<>();

    @Before
    public void setUp() {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setId("testClass");
        beanDefinition.setBeanClassName("com.gromoks.container.testentity.TestClass");
        Map<String, String> dependencies = new HashMap<>();
        dependencies.put("field1", "value1");
        dependencies.put("field2", "3");
        dependencies.put("field3", "true");
        beanDefinition.setDependencies(dependencies);
        beanDefinitionList.add(beanDefinition);
    }

    @Test
    public void readBeanDefinitionTest() {
        assertThat(new XMLBeanDefinitionReader(new File("src/test/resources/context.xml")).readBeanDefinition(), equalTo(beanDefinitionList));
    }
}
