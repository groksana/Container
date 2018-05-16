package com.gromoks.container.context;

import com.gromoks.container.entity.BeanDefinition;
import com.gromoks.container.exception.BeanInstantiationException;
import com.gromoks.container.exception.BeanNotFoundException;
import com.gromoks.container.injector.RefDependencyInjector;
import com.gromoks.container.injector.ValueDependencyInjector;
import com.gromoks.container.reader.BeanDefinitionReader;
import com.gromoks.container.reader.xml.XMLBeanDefinitionReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathApplicationContext implements ApplicationContext {
    private final Map<String, Object> beanMap = new HashMap<>();
    private BeanDefinitionReader beanDefinitionReader;
    private List<BeanDefinition> beanDefinitionList;

    public ClassPathApplicationContext() {
    }

    public ClassPathApplicationContext(String contextPath) {
        this(new String[]{contextPath});
    }

    public ClassPathApplicationContext(String[] pathList) {
        beanDefinitionReader = new XMLBeanDefinitionReader(pathList);
        start();
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        int count = 0;
        Object bean = null;
        for (Map.Entry<String, Object> pair : beanMap.entrySet()) {
            if (pair.getValue().getClass() == clazz) {
                count++;
                bean = pair.getValue();
            }
        }

        if (count == 1) {
            return clazz.cast(bean);
        } else if (count > 1) {
            throw new BeanInstantiationException("There are more then 1 bean with requested Class: " + clazz);
        } else {
            throw new BeanNotFoundException("Bean with requested Class is absent: " + clazz);
        }
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        Object bean = beanMap.get(name);

        if (bean != null) {
            return clazz.cast(bean);
        }
        throw new BeanNotFoundException("Bean with requested Class and Name is absent: " + clazz + " - " + name);
    }

    @Override
    public Object getBean(String name) {
        Object bean = beanMap.get(name);
        if (bean != null) {
            return bean;
        } else {
            throw new BeanNotFoundException("Bean with requested Name is absent: " + name);
        }
    }

    @Override
    public List<String> getBeanNames() {
        return new ArrayList<>(beanMap.keySet());
    }

    @Override
    public void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader) {
        this.beanDefinitionReader = beanDefinitionReader;
    }

    @Override
    public void start() {
        if (beanDefinitionList != null) {
            throw new RuntimeException("Already started");
        }
        beanDefinitionList = beanDefinitionReader.readBeanDefinition();
        createBeansFromBeanDefinitions();
        //injectDependencies();
        //injectRefDependencies();
        new ValueDependencyInjector(beanMap, beanDefinitionList).inject();
        new RefDependencyInjector(beanMap, beanDefinitionList).inject();
    }

    private void createBeansFromBeanDefinitions() {
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                Object object = clazz.newInstance();
                beanMap.put(beanDefinition.getId(), object);
            } catch (Exception e) {
                throw new BeanInstantiationException("Bean can't be instantiated. Bean Id = " + beanDefinition.getId(), e);
            }
        }
    }
}
