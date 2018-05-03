package com.gromoks.container.impl;

import com.gromoks.container.ApplicationContext;
import com.gromoks.container.entity.BeanDefinition;
import com.gromoks.container.exception.BeanInstantiationException;
import com.gromoks.container.reader.BeanDefinitionReader;
import com.gromoks.container.reader.impl.XMLBeanDefinitionReader;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClassPathApplicationContext implements ApplicationContext {
    private static final String INT_VALUE = "int";
    private static final String DOUBLE_VALUE = "double";
    private static final String LONG_VALUE = "long";
    private static final String BOOLEAN_VALUE = "boolean";

    private final Map<String, Object> beanMap = new ConcurrentHashMap<>();
    private BeanDefinitionReader beanDefinitionReader;
    private List<BeanDefinition> beanDefinitionList = new CopyOnWriteArrayList<>();

    public ClassPathApplicationContext() {
    }

    public ClassPathApplicationContext(String contextPath) {
        File file = new File(contextPath);
        beanDefinitionReader = new XMLBeanDefinitionReader(file);
        beanDefinitionList = beanDefinitionReader.readBeanDefinition();

        createBeansFromBeanDefinitions();
        injectDependencies();
        injectRefDependencies();
    }

    public ClassPathApplicationContext(String[] pathList) {
        for (String path : pathList) {
            File file = new File(path);
            beanDefinitionReader = new XMLBeanDefinitionReader(file);
            beanDefinitionList.addAll(beanDefinitionReader.readBeanDefinition());
        }

        createBeansFromBeanDefinitions();
        injectDependencies();
        injectRefDependencies();
    }

    public <T> T getBean(Class<T> clazz) {
        for (Map.Entry<String, Object> pair : beanMap.entrySet()) {
            if (pair.getValue().getClass() == clazz) {
                return (T) pair.getValue();
            } else {
                throw new IllegalArgumentException("Bean with required Class is absent");
            }
        }
        throw new RuntimeException("Bean map is empty");
    }

    public <T> T getBean(String name, Class<T> clazz) {
        for (Map.Entry<String, Object> pair : beanMap.entrySet()) {
            if (pair.getKey().equals(name)) {
                return getBean(clazz);
            } else {
                throw new IllegalArgumentException("Bean with required Class and Name is absent");
            }
        }
        throw new RuntimeException("Bean map is empty");
    }

    public Object getBean(String name) {
        return beanMap.get(name);
    }

    public List<String> getBeanNames() {
        return new ArrayList<>(beanMap.keySet());
    }

    public void setBeanDefinitionReader(BeanDefinitionReader beanDefinitionReader) {
        this.beanDefinitionReader = beanDefinitionReader;
    }

    @Override
    public void manualStart() {
        if (beanDefinitionList == null) {
            beanDefinitionList = beanDefinitionReader.readBeanDefinition();
            createBeansFromBeanDefinitions();
            injectDependencies();
            injectRefDependencies();
        }
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
                Object value = pair.getValue();

                Field field = clazz.getDeclaredField(key);
                String fieldType = field.getType().getSimpleName();

                Class<?>[] paramTypes = {field.getType()};
                Method method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(key), paramTypes);
                switch (fieldType) {
                    case INT_VALUE:
                        method.invoke(object, Integer.valueOf((String) value));
                        break;
                    case DOUBLE_VALUE:
                        method.invoke(object, Double.valueOf((String) value));
                        break;
                    case LONG_VALUE:
                        method.invoke(object, Long.valueOf((String) value));
                        break;
                    case BOOLEAN_VALUE:
                        method.invoke(object, Boolean.valueOf((String) value));
                        break;
                    default:
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

                Method method = clazz.getDeclaredMethod("set" + StringUtils.capitalize(key), refObject.getClass());

                method.invoke(object, refObject);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            throw new BeanInstantiationException("Bean can't be injected by ref dependencies. Bean Id = " + beanDefinition.getId(), e);
        }
    }
}
