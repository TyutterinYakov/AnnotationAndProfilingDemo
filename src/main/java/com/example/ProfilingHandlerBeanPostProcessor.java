package com.example;

import com.example.annotation.Profiling;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProfilingHandlerBeanPostProcessor implements BeanPostProcessor {

    private Map<String, Class> map = new HashMap<>();
    private final ProfilingControllerMBean profilingController;

    @Autowired
    public ProfilingHandlerBeanPostProcessor(ProfilingControllerMBean profilingController) throws Exception{
        this.profilingController = profilingController;
        MBeanServer platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
        platformMBeanServer.registerMBean(
                profilingController,
                new ObjectName("profiling", "name", "controller"));
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> aClass = bean.getClass();
        if(aClass.isAnnotationPresent(Profiling.class)){
            map.put(beanName, aClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class aClass = map.get(beanName);
        if(aClass!=null){
            return Proxy.newProxyInstance(aClass.getClassLoader(), aClass.getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if(profilingController.isEnabled()) {
                        System.out.println(method.getName());
                        Long before = System.nanoTime();
                        Object retVal = method.invoke(bean, args);
                        Long after = System.nanoTime();
                        System.out.println(after - before);
                        System.out.println("ВсЁ");
                        return retVal;
                    } else {
                        return method.invoke(bean, args);
                    }
                }
            });
        }
        return bean;
    }
}
