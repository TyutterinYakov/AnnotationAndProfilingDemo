package com.example;

import com.example.annotation.DeprecatedClass;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DeprecationHandlerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
            throws BeansException {
        Arrays.stream(configurableListableBeanFactory.getBeanDefinitionNames()).forEach((nameBeanDefinition)->{
            BeanDefinition beanDefinition = configurableListableBeanFactory.getBeanDefinition(nameBeanDefinition);
            String beanClassName = beanDefinition.getBeanClassName();
            try {
                if(beanClassName!=null) {
                    Class<?> beanClass = Class.forName(beanClassName);
                    DeprecatedClass annotation = beanClass.getAnnotation(DeprecatedClass.class);
                    if (annotation != null) {
                        beanDefinition.setBeanClassName(annotation.newImpl().getName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
