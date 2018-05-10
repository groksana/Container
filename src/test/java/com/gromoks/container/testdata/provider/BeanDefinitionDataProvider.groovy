package com.gromoks.container.testdata.provider

import com.gromoks.container.entity.BeanDefinition
import org.testng.annotations.DataProvider

class BeanDefinitionDataProvider {
    @DataProvider(name = "provideBeanDefinitions")
    static Object[][] provideBeanDefinitions() {
        def beanDefinitionList = new ArrayList<>()

        // productService Bean
        def dependenciesFirst = [stringField: 'value1', intField: '3', booleanField: 'true']
        BeanDefinition beanDefinitionFirst = new BeanDefinition(
                id: 'productService', beanClassName: 'com.gromoks.container.testdata.ProductService',
                dependencies: dependenciesFirst, refDependencies: null
        )
        beanDefinitionList.add(beanDefinitionFirst)

        // emailService Bean
        def dependenciesSecond = [protocol: 'POP3', port: '3000']
        BeanDefinition beanDefinitionSecond = new BeanDefinition(
                id: 'emailService', beanClassName: 'com.gromoks.container.testdata.EmailService',
                dependencies: dependenciesSecond, refDependencies: null
        )
        beanDefinitionList.add(beanDefinitionSecond)

        // userService Bean
        def refDependenciesThird = [emailService: 'emailService']
        BeanDefinition beanDefinitionThird = new BeanDefinition(
                id: 'userService', beanClassName: 'com.gromoks.container.testdata.UserService',
                dependencies: null, refDependencies: refDependenciesThird
        )
        beanDefinitionList.add(beanDefinitionThird)

        // secondProductService Bean
        def dependenciesForth = [stringField: 'value2', intField: '4']
        BeanDefinition beanDefinitionForth = new BeanDefinition(
                id: 'secondProductService', beanClassName: 'com.gromoks.container.testdata.ProductService',
                dependencies: dependenciesForth, refDependencies: null
        )
        beanDefinitionList.add(beanDefinitionForth)

        def array = new Object[1][]
        array[0] = [beanDefinitionList] as Object[]
        return array
    }

    @DataProvider(name = "provideBeanDefinitionsPaths")
    static Object[][] provideBeanDefinitionsPaths() {
        def beanDefinitionList = new ArrayList<>()

        // productService Bean
        def dependenciesFirst = [stringField: 'value1', intField: '3', booleanField: 'true']
        BeanDefinition beanDefinitionFirst = new BeanDefinition(
                id: 'productService', beanClassName: 'com.gromoks.container.testdata.ProductService',
                dependencies: dependenciesFirst, refDependencies: null
        )
        beanDefinitionList.add(beanDefinitionFirst)

        // userService Bean
        def refDependenciesSecond = [emailService: 'emailService']
        BeanDefinition beanDefinitionSecond = new BeanDefinition(
                id: 'userService', beanClassName: 'com.gromoks.container.testdata.UserService',
                dependencies: null, refDependencies: refDependenciesSecond
        )
        beanDefinitionList.add(beanDefinitionSecond)

        // secondProductService Bean
        def dependenciesThird = [stringField: 'value2', intField: '4']
        BeanDefinition beanDefinitionThird = new BeanDefinition(
                id: 'secondProductService', beanClassName: 'com.gromoks.container.testdata.ProductService',
                dependencies: dependenciesThird, refDependencies: null
        )
        beanDefinitionList.add(beanDefinitionThird)

        // emailService Bean
        def dependenciesForth = [protocol: 'POP3', port: '3000']
        BeanDefinition beanDefinitionForth = new BeanDefinition(
                id: 'emailService', beanClassName: 'com.gromoks.container.testdata.EmailService',
                dependencies: dependenciesForth, refDependencies: null
        )
        beanDefinitionList.add(beanDefinitionForth)

        def array = new Object[1][]
        array[0] = [beanDefinitionList] as Object[]
        return array
    }

    @DataProvider(name = "provideBeanNames")
    static Object[][] provideBeanNames() {
        def beanNames = ['secondProductService', 'emailService', 'productService', 'userService']

        def array = new Object[1][]
        array[0] = [beanNames] as Object[]
        return array
    }

    @DataProvider(name = 'provideBeanDefinitionInt')
    static Object[][] provideBeanDefinitionInt() {
        def beanDefinition = new BeanDefinition(
                id: 'dataTypeHolder', beanClassName: 'com.gromoks.container.testdata.DataTypeHolder',
                dependencies: [intType: '300'], refDependencies: null
        )

        def array = new Object[1][]
        array[0] = [beanDefinition]
        return array
    }

    @DataProvider(name = 'provideBeanDefinitionInteger')
    static Object[][] provideBeanDefinitionInteger() {
        def beanDefinition = new BeanDefinition(
                id: 'dataTypeHolder', beanClassName: 'com.gromoks.container.testdata.DataTypeHolder',
                dependencies: [integerType: '300'], refDependencies: null
        )

        def array = new Object[1][]
        array[0] = [beanDefinition]
        return array
    }

    @DataProvider(name = 'provideBeanDefinitionDouble')
    static Object[][] provideBeanDefinitionDouble() {
        def beanDefinition = new BeanDefinition(
                id: 'dataTypeHolder', beanClassName: 'com.gromoks.container.testdata.DataTypeHolder',
                dependencies: [doubleType: '300.55'], refDependencies: null
        )

        def array = new Object[1][]
        array[0] = [beanDefinition]
        return array
    }

    @DataProvider(name = 'provideBeanDefinitionDoubleBig')
    static Object[][] provideBeanDefinitionDoubleBig() {
        def beanDefinition = new BeanDefinition(
                id: 'dataTypeHolder', beanClassName: 'com.gromoks.container.testdata.DataTypeHolder',
                dependencies: [doubleBigType: '300.55'], refDependencies: null
        )

        def array = new Object[1][]
        array[0] = [beanDefinition]
        return array
    }

    @DataProvider(name = 'provideBeanDefinitionLong')
    static Object[][] provideBeanDefinitionLong() {
        def beanDefinition = new BeanDefinition(
                id: 'dataTypeHolder', beanClassName: 'com.gromoks.container.testdata.DataTypeHolder',
                dependencies: [longType: '5555555555555'], refDependencies: null
        )

        def array = new Object[1][]
        array[0] = [beanDefinition]
        return array
    }

    @DataProvider(name = 'provideBeanDefinitionLongBig')
    static Object[][] provideBeanDefinitionLongBig() {
        def beanDefinition = new BeanDefinition(
                id: 'dataTypeHolder', beanClassName: 'com.gromoks.container.testdata.DataTypeHolder',
                dependencies: [longBigType: '5555555555555'], refDependencies: null
        )

        def array = new Object[1][]
        array[0] = [beanDefinition]
        return array
    }

    @DataProvider(name = 'provideBeanDefinitionBoolean')
    static Object[][] provideBeanDefinitionBoolean() {
        def beanDefinition = new BeanDefinition(
                id: 'dataTypeHolder', beanClassName: 'com.gromoks.container.testdata.DataTypeHolder',
                dependencies: [booleanType: 'true'], refDependencies: null
        )

        def array = new Object[1][]
        array[0] = [beanDefinition]
        return array
    }

    @DataProvider(name = 'provideBeanDefinitionBooleanBig')
    static Object[][] provideBeanDefinitionBooleanBig() {
        def beanDefinition = new BeanDefinition(
                id: 'dataTypeHolder', beanClassName: 'com.gromoks.container.testdata.DataTypeHolder',
                dependencies: [booleanBigType: 'true'], refDependencies: null
        )

        def array = new Object[1][]
        array[0] = [beanDefinition]
        return array
    }

    @DataProvider(name = 'provideBeanDefinitionString')
    static Object[][] provideBeanDefinitionString() {
        def beanDefinition = new BeanDefinition(
                id: 'dataTypeHolder', beanClassName: 'com.gromoks.container.testdata.DataTypeHolder',
                dependencies: [stringType: 'hello'], refDependencies: null
        )

        def array = new Object[1][]
        array[0] = [beanDefinition]
        return array
    }

    @DataProvider(name = 'provideBeanDefinitionEmailService')
    static Object[][] provideBeanDefinitionEmailService() {
        def beanDefinition = new BeanDefinition(
                id: 'dataTypeHolder', beanClassName: 'com.gromoks.container.testdata.DataTypeHolder',
                dependencies: null, refDependencies: [emailService: 'emailService']
        )

        def array = new Object[1][]
        array[0] = [beanDefinition]
        return array
    }
}
