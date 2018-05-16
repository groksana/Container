package com.gromoks.container.injector;

import com.gromoks.container.entity.BeanDefinition;
import com.gromoks.container.exception.BeanInstantiationException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ValueDependencyInjector extends Injector {
    public ValueDependencyInjector(Map<String, Object> beanMap, List<BeanDefinition> beanDefinitionList) {
        super(beanMap, beanDefinitionList);
    }

    @Override
    public void injectDependencies(BeanDefinition beanDefinition, Object object) {
        Class<?> clazz = object.getClass();

        if (beanDefinition.getDependencies() != null) {
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
    }
}
