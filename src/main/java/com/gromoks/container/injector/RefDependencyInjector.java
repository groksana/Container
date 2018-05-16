package com.gromoks.container.injector;

import com.gromoks.container.entity.BeanDefinition;
import com.gromoks.container.exception.BeanInstantiationException;
import com.gromoks.container.exception.BeanNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class RefDependencyInjector extends Injector {
    public RefDependencyInjector(Map<String, Object> beanMap, List<BeanDefinition> beanDefinitionList) {
        super(beanMap, beanDefinitionList);
    }

    @Override
    public void injectDependencies(BeanDefinition beanDefinition, Object object) {
        Class<?> clazz = object.getClass();

        if (beanDefinition.getRefDependencies() != null) {

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
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BeanInstantiationException("Bean can't be injected by ref dependencies. Bean Id = " + beanDefinition.getId(), e);
            }
        }
    }
}
