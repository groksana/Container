package com.gromoks.container.context;

import com.gromoks.container.entity.BeanDefinition;
import com.gromoks.container.exception.BeanInstantiationException;
import com.gromoks.container.exception.BeanNotFoundException;
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
        injectDependencies();
        injectRefDependencies();
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

    void injectDependencies(BeanDefinition beanDefinition, Object object) {
        Class<?> clazz = object.getClass();

        try {
            for (Map.Entry<String, String> pair : beanDefinition.getDependencies().entrySet()) {
                String key = pair.getKey();
                String value = pair.getValue();

                Field field = clazz.getDeclaredField(key);
                Class fieldType = field.getType();

                Method method;
                try {
                    method = clazz.getDeclaredMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), field.getType());
                } catch (NoSuchMethodException e) {
                    throw new BeanInstantiationException("No setter was found in " + clazz + " for field " + key, e);
                }

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
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            throw new BeanInstantiationException("Bean can't be injected by value dependencies. Bean Id = " + beanDefinition.getId(), e);
        }
    }

    void injectRefDependencies(BeanDefinition beanDefinition, Object object) {
        Class<?> clazz = object.getClass();

        try {
            for (Map.Entry<String, String> pair : beanDefinition.getRefDependencies().entrySet()) {
                String key = pair.getKey();
                String value = pair.getValue();

                Object refObject = beanMap.get(value);
                if (refObject == null) {
                    throw new BeanNotFoundException("No such bean was registered: " + value);
                }

                Method method;
                try {
                    method = clazz.getDeclaredMethod("set" + key.substring(0, 1).toUpperCase() + key.substring(1), refObject.getClass());
                } catch (NoSuchMethodException e) {
                    throw new BeanInstantiationException("No setter was found in " + clazz + " for field " + key, e);
                }
                method.invoke(object, refObject);
            }
        }  catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeanInstantiationException("Bean can't be injected by ref dependencies. Bean Id = " + beanDefinition.getId(), e);
        }
    }
}
