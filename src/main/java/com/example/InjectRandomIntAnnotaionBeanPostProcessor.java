package com.example;

import com.example.annotation.InjectRandomInt;
import com.example.annotation.Profiling;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Random;

@Component
public class InjectRandomIntAnnotaionBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] declaredFields = bean.getClass().getDeclaredFields();

        Arrays.stream(declaredFields).forEach((f)->{
            InjectRandomInt annotation = f.getAnnotation(InjectRandomInt.class);
            if(annotation!=null){
                int min = annotation.min();
                int max = annotation.max();
                Random random = new Random();
                int i = min+random.nextInt(max-min);
                f.setAccessible(true);
                ReflectionUtils.setField(f, bean, i);
            }
        });
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
