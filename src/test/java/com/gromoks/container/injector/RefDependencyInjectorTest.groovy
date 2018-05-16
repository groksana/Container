package com.gromoks.container.injector

import com.gromoks.container.entity.BeanDefinition
import com.gromoks.container.exception.BeanNotFoundException
import com.gromoks.container.testdata.DataTypeHolder
import com.gromoks.container.testdata.provider.BeanDefinitionDataProvider
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

class RefDependencyInjectorTest {
    Map<String, Object> beanMap = new HashMap<>()
    List<BeanDefinition> beanDefinitionList

    @Test(dataProvider = "provideBeanDefinitionEmailService",
            dataProviderClass = BeanDefinitionDataProvider.class,
            expectedExceptionsMessageRegExp = "No such bean was registered: emailService",
            expectedExceptions = BeanNotFoundException.class)
    void injectRefDependenciesEmailServiceTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        Injector injector = new RefDependencyInjector(beanMap, beanDefinitionList)
        injector.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getEmailService(), beanDefinition.refDependencies.get("emailService"))
    }
}
