package com.gromoks.container.injector

import com.gromoks.container.entity.BeanDefinition
import com.gromoks.container.testdata.DataTypeHolder
import com.gromoks.container.testdata.provider.BeanDefinitionDataProvider
import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

class ValueDependencyInjectorTest {
    Map<String, Object> beanMap
    List<BeanDefinition> beanDefinitionList

    @Test(dataProvider = "provideBeanDefinitionInt", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesIntTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        Injector injector = new ValueDependencyInjector(beanMap, beanDefinitionList)
        injector.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getIntType(), Integer.parseInt(beanDefinition.dependencies.get("intType")))
    }

    @Test(dataProvider = "provideBeanDefinitionInteger", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesIntegerTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        Injector injector = new ValueDependencyInjector(beanMap, beanDefinitionList)
        injector.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getIntegerType(), Integer.parseInt(beanDefinition.dependencies.get("integerType")))
    }

    @Test(dataProvider = "provideBeanDefinitionDouble", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesDoubleTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        Injector injector = new ValueDependencyInjector(beanMap, beanDefinitionList)
        injector.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getDoubleType(), Double.parseDouble(beanDefinition.dependencies.get("doubleType")))
    }

    @Test(dataProvider = "provideBeanDefinitionDoubleBig", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesDoubleBigTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        Injector injector = new ValueDependencyInjector(beanMap, beanDefinitionList)
        injector.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getDoubleBigType(), Double.parseDouble(beanDefinition.dependencies.get("doubleBigType")))
    }

    @Test(dataProvider = "provideBeanDefinitionLong", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesLongTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        Injector injector = new ValueDependencyInjector(beanMap, beanDefinitionList)
        injector.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getLongType(), Long.parseLong(beanDefinition.dependencies.get("longType")))
    }

    @Test(dataProvider = "provideBeanDefinitionLongBig", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesLongBigTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        Injector injector = new ValueDependencyInjector(beanMap, beanDefinitionList)
        injector.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getLongBigType(), Long.parseLong(beanDefinition.dependencies.get("longBigType")))
    }

    @Test(dataProvider = "provideBeanDefinitionBoolean", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesBooleanTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        Injector injector = new ValueDependencyInjector(beanMap, beanDefinitionList)
        injector.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getBooleanType(), Boolean.parseBoolean(beanDefinition.dependencies.get("booleanType")))
    }

    @Test(dataProvider = "provideBeanDefinitionBooleanBig", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesBooleanBigTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        Injector injector = new ValueDependencyInjector(beanMap, beanDefinitionList)
        injector.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getBooleanBigType(), Boolean.parseBoolean(beanDefinition.dependencies.get("booleanBigType")))
    }

    @Test(dataProvider = "provideBeanDefinitionString", dataProviderClass = BeanDefinitionDataProvider.class)
    void injectDependenciesStringTest(BeanDefinition beanDefinition) {
        DataTypeHolder dataTypeHolder = new DataTypeHolder()
        Injector injector = new ValueDependencyInjector(beanMap, beanDefinitionList)
        injector.injectDependencies(beanDefinition, dataTypeHolder)

        assertEquals(dataTypeHolder.getStringType(), beanDefinition.dependencies.get("stringType"))
    }
}
