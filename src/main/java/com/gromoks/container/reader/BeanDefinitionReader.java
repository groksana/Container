package com.gromoks.container.reader;

import com.gromoks.container.entity.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {
    List<BeanDefinition> readBeanDefinition();
}
