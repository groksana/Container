package com.gromoks.container.injector;

import com.gromoks.container.entity.BeanDefinition;

import java.util.List;
import java.util.Map;

public abstract class Injector {
    protected Map<String, Object> beanMap;
    protected List<BeanDefinition> beanDefinitionList;

    public Injector(Map<String, Object> beanMap, List<BeanDefinition> beanDefinitionList) {
        this.beanMap = beanMap;
        this.beanDefinitionList = beanDefinitionList;
    }

    public void inject() {
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            Object object = beanMap.get(beanDefinition.getId());
            injectDependencies(beanDefinition, object);
        }
    }

    abstract void injectDependencies(BeanDefinition beanDefinition, Object bean);
}
