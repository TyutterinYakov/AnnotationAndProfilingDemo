package com.example;

import com.example.annotation.PostProxy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class PostProxyInvokerContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private final ConfigurableListableBeanFactory factory;

    public PostProxyInvokerContextListener(ConfigurableListableBeanFactory factory) {
        this.factory = factory;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        String[] beanDefinitionNames = factory.getBeanDefinitionNames();
        Arrays.stream(beanDefinitionNames).filter(n->n!=null).forEach((bdn)->{
            BeanDefinition beanDefinition = factory.getBeanDefinition(bdn);
            String originalClassName = beanDefinition.getBeanClassName();
                if(originalClassName!=null) {
                    try {
                        Class<?> originalClass = Class.forName(originalClassName);
                        Method[] methods = originalClass.getMethods();
                        Arrays.stream(methods).forEach((method) -> {
                            if (method.isAnnotationPresent(PostProxy.class)) {
                                if (originalClassName != null) {
                                    try {
                                        Object bean = factory.getBean(bdn);
                                        Method currentMethod = bean.getClass().getMethod(
                                                method.getName(),
                                                method.getParameterTypes());
                                        currentMethod.invoke(bean);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        });
                    } catch(ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        });
    }
}
