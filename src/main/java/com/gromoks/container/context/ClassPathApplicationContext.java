package com.gromoks.container.context;

import com.gromoks.container.entity.BeanDefinition;
import com.gromoks.container.exception.BeanInstantiationException;
import com.gromoks.container.reader.BeanDefinitionReader;
import com.gromoks.container.reader.xml.XMLBeanDefinitionReader;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
        for (Map.Entry<String, Object> pair : beanMap.entrySet()) {
            if (pair.getValue().getClass() == clazz) {
                return clazz.cast(pair.getValue());
            }
        }
        throw new RuntimeException("Bean with required Class is absent");
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) {
        for (Map.Entry<String, Object> pair : beanMap.entrySet()) {
            if (pair.getKey().equals(name) && pair.getValue().getClass() == clazz) {
                return clazz.cast(pair.getValue());
            }
        }
        throw new RuntimeException("Bean with required Class and Name is absent");
    }

    @Override
    public Object getBean(String name) {
        return beanMap.get(name);
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
        injectDependencies();
        injectRefDependencies();
    }

    private void createBeansFromBeanDefinitions() {
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                Object object = clazz.newInstance();
                beanMap.put(beanDefinition.getId(), object);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new BeanInstantiationException("Bean can't be instantiated. Bean Id = " + beanDefinition.getId(), e);
            }
        }
    }

    private void injectDependencies() {
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            if (beanDefinition.getDependencies() != null) {
                Object object = beanMap.get(beanDefinition.getId());
                injectDependencies(beanDefinition, object);
            }
        }
    }

    private void injectRefDependencies() {
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            if (beanDefinition.getRefDependencies() != null) {
                Object object = beanMap.get(beanDefinition.getId());
                injectRefDependencies(beanDefinition, object);
            }
        }
    }

    private void injectDependencies(BeanDefinition beanDefinition, Object object) {
        try {
            Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());

            for (Map.Entry<String, String> pair : beanDefinition.getDependencies().entrySet()) {
                String key = pair.getKey();
                String value = pair.getValue();

                Field field = clazz.getDeclaredField(key);
                Class fieldType = field.getType();

                Method method = clazz.getDeclaredMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), field.getType());
                if (fieldType == Integer.class || fieldType == int.class) {
                    method.invoke(object, Integer.valueOf(value));
                } else if (fieldType == Double.class || fieldType == double.class) {
                    method.invoke(object, Double.valueOf(value));
                } else if (fieldType == Long.class || fieldType == long.class) {
                    method.invoke(object, Long.valueOf(value));
                } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                    method.invoke(object, Boolean.valueOf(value));
                } else {
                    method.invoke(object, value);
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            throw new BeanInstantiationException("Bean can't be injected by value dependencies. Bean Id = " + beanDefinition.getId(), e);
        }
    }

    private void injectRefDependencies(BeanDefinition beanDefinition, Object object) {
        try {
            Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
            for (Map.Entry<String, String> pair : beanDefinition.getRefDependencies().entrySet()) {
                String key = pair.getKey();
                String value = pair.getValue();

                Object refObject = beanMap.get(value);

                Method method = clazz.getDeclaredMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), refObject.getClass());

                method.invoke(object, refObject);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            throw new BeanInstantiationException("Bean can't be injected by ref dependencies. Bean Id = " + beanDefinition.getId(), e);
        }
    }
}
