package com.gromoks.container.impl;

import com.gromoks.container.testentity.TestClass;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

import static org.junit.Assert.*;

public class ClassPathApplicationContextTest {
    private TestClass testClass = new TestClass();

    @Before
    public void setUp() {
        testClass = new TestClass();
        testClass.setField1("value1");
        testClass.setField2(3);
        testClass.setField3(true);
    }

    @Test
    public void getBeanTest() {
        assertThat(new ClassPathApplicationContext("src/test/resources/context.xml").getBean("testClass"), equalTo(testClass));
    }

    @Test
    public void getGenericBeanTest() {
        assertThat(new ClassPathApplicationContext("src/test/resources/context.xml").getBean(TestClass.class), equalTo(testClass));
    }

    @Test
    public void getGenericBeanByNameTest() {
        assertThat(new ClassPathApplicationContext("src/test/resources/context.xml").getBean("testClass", TestClass.class), equalTo(testClass));
    }
}