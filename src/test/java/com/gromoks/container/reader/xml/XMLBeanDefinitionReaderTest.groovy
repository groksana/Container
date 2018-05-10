package com.gromoks.container.reader.xml

import com.gromoks.container.reader.BeanDefinitionReader
import com.gromoks.container.testdata.provider.BeanDefinitionDataProvider
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertEquals

class XMLBeanDefinitionReaderTest {
    @Test(dataProvider = "provideBeanDefinitions", dataProviderClass = BeanDefinitionDataProvider.class)
    void testGetBeanDefinitionsByPath(expectedBeanDefinitions) {
        BeanDefinitionReader beanDefinitionReader = new XMLBeanDefinitionReader("src/test/resources/context.xml")
        def actualBeanDefinitionList = beanDefinitionReader.readBeanDefinition()
        assertEquals(actualBeanDefinitionList, expectedBeanDefinitions)
    }


    @Test(dataProvider = "provideBeanDefinitionsPaths", dataProviderClass = BeanDefinitionDataProvider.class)
    void testGetBeanDefinitionsByPaths(expectedBeanDefinitions) {
        BeanDefinitionReader beanDefinitionReader = new XMLBeanDefinitionReader("src/test/resources/main-context.xml", "src/test/resources/email-context.xml")
        def actualBeanDefinitions = beanDefinitionReader.readBeanDefinition()
        assertEquals(actualBeanDefinitions, expectedBeanDefinitions)
    }
}
