package com.gromoks.container.context

import com.gromoks.container.entity.BeanDefinition
import com.gromoks.container.exception.BeanInstantiationException
import com.gromoks.container.exception.BeanNotFoundException
import com.gromoks.container.testdata.DataTypeHolder
import com.gromoks.container.testdata.ProductService
import com.gromoks.container.testdata.UserService
import com.gromoks.container.testdata.provider.BeanDefinitionDataProvider
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals
import static org.testng.Assert.assertTrue

class ClassPathApplicationContextITest {
    @Test
    void getBeanByClassTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/context.xml");
        def userService = applicationContext.getBean(UserService.class)
        assertTrue(userService instanceof UserService)
    }

    @Test
    void getBeanByClassPathsTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/main-context.xml",
                "src/test/resources/email-context.xml")
        def userService = applicationContext.getBean(UserService.class)
        assertTrue(userService instanceof UserService)
    }

    @Test(expectedExceptionsMessageRegExp = "There are more then 1 bean with requested Class: class com.gromoks.container.testdata.ProductService",
            expectedExceptions = BeanInstantiationException.class)
    void getBeanByClassMultipleBeansTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/context.xml");
        applicationContext.getBean(ProductService.class)
    }

    @Test(expectedExceptionsMessageRegExp = "Bean with requested Class is absent: class java.lang.String",
            expectedExceptions = BeanNotFoundException.class)
    void getBeanByClassNotExistsTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/context.xml");
        applicationContext.getBean(String.class)
    }

    @Test(expectedExceptionsMessageRegExp = "Bean with requested Class is absent: class java.lang.String",
            expectedExceptions = BeanNotFoundException.class)
    void getBeanByClassNotExistsPathsTest() {
        ApplicationContext context = new ClassPathApplicationContext("src/test/resources/main-context.xml",
                "src/test/resources/email-context.xml")
        context.getBean(String.class)
    }

    @Test
    void getBeanByIdTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/context.xml");
        def userService = applicationContext.getBean("userService")
        assertTrue(userService instanceof UserService)
    }

    @Test
    void getBeanByIdPathsTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/main-context.xml",
                "src/test/resources/email-context.xml")
        def userService = applicationContext.getBean("userService")
        assertTrue(userService instanceof UserService)
    }

    @Test(expectedExceptionsMessageRegExp = "Bean with requested Name is absent: userService",
            expectedExceptions = BeanNotFoundException.class)
    void getBeanByIdBeanNotRegisteredTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/email-context.xml");
        applicationContext.getBean("userService")
    }

    @Test(expectedExceptionsMessageRegExp = "Bean with requested Name is absent: string",
            expectedExceptions = BeanNotFoundException.class)
    void getBeanByIdBeanNotRegisteredPathsTest() {
        ApplicationContext context = new ClassPathApplicationContext("src/test/resources/main-context.xml",
                "src/test/resources/email-context.xml")
        context.getBean("string")
    }

    @Test
    void getBeanByIdAndClassTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/context.xml");
        def productService = applicationContext.getBean("secondProductService", ProductService.class)
        assertTrue(productService instanceof ProductService)
    }

    @Test
    void getBeanByIdAndClassPathsTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/main-context.xml",
                "src/test/resources/email-context.xml")
        def productService = applicationContext.getBean("secondProductService", ProductService.class)
        assertTrue(productService instanceof ProductService)
    }

    @Test(expectedExceptionsMessageRegExp = "Bean with requested Class and Name is absent: class java.lang.String - string",
            expectedExceptions = BeanNotFoundException.class)
    void getBeanByIdAndClassNotExistsTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/context.xml");
        applicationContext.getBean("string", String.class)
    }

    @Test(expectedExceptionsMessageRegExp = "Bean with requested Class and Name is absent: class java.lang.String - string",
            expectedExceptions = BeanNotFoundException.class)
    void getBeanByIdAndClassNotExistsPathsTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/main-context.xml",
                "src/test/resources/email-context.xml")
        applicationContext.getBean("string", String.class)
    }

    @Test(expectedExceptionsMessageRegExp = "No setter was found in class com.gromoks.container.testdata.UserServiceNoSetter for field emailService",
            expectedExceptions = BeanInstantiationException.class)
    void getBeanByIdNoSetterTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/nosetter-context.xml")
        applicationContext.getBean("userServiceNoSetter")
    }

    @Test(expectedExceptionsMessageRegExp = "No setter was found in class com.gromoks.container.testdata.UserServiceNoSetter for field emailService",
            expectedExceptions = BeanInstantiationException.class)
    void getBeanByIdNoSetterPathsTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/nosetter-context.xml",
                "src/test/resources/email-context.xml")
        applicationContext.getBean("userServiceNoSetter")
    }

    @Test(expectedExceptionsMessageRegExp = "No such bean was registered: emailServiceNotExists",
            expectedExceptions = BeanNotFoundException.class)
    void getBeanByIdNoRefRegisteredTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/incorrect-ref-context.xml")
        applicationContext.getBean("userServiceIncorrectRef")
    }

    @Test(expectedExceptionsMessageRegExp = "No such bean was registered: emailServiceNotExists",
            expectedExceptions = BeanNotFoundException.class)
    void getBeanByIdNoRefRegisteredPathsTest() {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/incorrect-ref-context.xml",
                "src/test/resources/email-context.xml")
        applicationContext.getBean("userServiceIncorrectRef")
    }

    @Test(dataProvider = "provideBeanNames", dataProviderClass = BeanDefinitionDataProvider.class)
    void getBeanNamesTest(List<String> expectedBeanNames) {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/context.xml")
        def actualBeanNames = applicationContext.getBeanNames()
        assertEquals(actualBeanNames, expectedBeanNames)
    }

    @Test(dataProvider = "provideBeanNames", dataProviderClass = BeanDefinitionDataProvider.class)
    void getBeanNamesPathsTest(List<String> expectedBeanNames) {
        ApplicationContext applicationContext = new ClassPathApplicationContext("src/test/resources/email-context.xml",
                "src/test/resources/main-context.xml")
        def actualBeanNames = applicationContext.getBeanNames()
        assertEquals(actualBeanNames, expectedBeanNames)
    }

    @Test(dataProvider = "provideBeanDefinitionInt", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesIntTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        ApplicationContext applicationContext = new ClassPathApplicationContext()
        applicationContext.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getIntType(), Integer.parseInt(beanDefinition.dependencies.get("intType")))
    }

    @Test(dataProvider = "provideBeanDefinitionInteger", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesIntegerTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        ApplicationContext applicationContext = new ClassPathApplicationContext()
        applicationContext.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getIntegerType(), Integer.parseInt(beanDefinition.dependencies.get("integerType")))
    }

    @Test(dataProvider = "provideBeanDefinitionDouble", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesDoubleTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        ApplicationContext applicationContext = new ClassPathApplicationContext()
        applicationContext.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getDoubleType(), Double.parseDouble(beanDefinition.dependencies.get("doubleType")))
    }

    @Test(dataProvider = "provideBeanDefinitionDoubleBig", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesDoubleBigTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        ApplicationContext applicationContext = new ClassPathApplicationContext()
        applicationContext.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getDoubleBigType(), Double.parseDouble(beanDefinition.dependencies.get("doubleBigType")))
    }

    @Test(dataProvider = "provideBeanDefinitionLong", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesLongTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        ApplicationContext applicationContext = new ClassPathApplicationContext()
        applicationContext.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getLongType(), Long.parseLong(beanDefinition.dependencies.get("longType")))
    }

    @Test(dataProvider = "provideBeanDefinitionLongBig", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesLongBigTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        ApplicationContext applicationContext = new ClassPathApplicationContext()
        applicationContext.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getLongBigType(), Long.parseLong(beanDefinition.dependencies.get("longBigType")))
    }

    @Test(dataProvider = "provideBeanDefinitionBoolean", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesBooleanTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        ApplicationContext applicationContext = new ClassPathApplicationContext()
        applicationContext.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getBooleanType(), Boolean.parseBoolean(beanDefinition.dependencies.get("booleanType")))
    }

    @Test(dataProvider = "provideBeanDefinitionBooleanBig", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesBooleanBigTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        ApplicationContext applicationContext = new ClassPathApplicationContext()
        applicationContext.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getBooleanBigType(), Boolean.parseBoolean(beanDefinition.dependencies.get("booleanBigType")))
    }

    @Test(dataProvider = "provideBeanDefinitionString", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesStringTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        ApplicationContext applicationContext = new ClassPathApplicationContext()
        applicationContext.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getStringType(), beanDefinition.dependencies.get("stringType"))
    }

    @Test(dataProvider = "provideBeanDefinitionEmailService",
            dataProviderClass = BeanDefinitionDataProvider.class,
            expectedExceptionsMessageRegExp = "No such bean was registered: emailService",
            expectedExceptions = BeanNotFoundException.class)
    void injectRefDependenciesEmailServiceTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        ApplicationContext applicationContext = new ClassPathApplicationContext()
        applicationContext.injectRefDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getEmailService(), beanDefinition.refDependencies.get("emailService"))
    }
}
