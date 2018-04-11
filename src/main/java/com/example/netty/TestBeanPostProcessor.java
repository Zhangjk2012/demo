package com.example.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Created by zhangjiangke on 2017/5/8.
 */
@Component
public class TestBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware, SmartInitializingSingleton {

    Logger logger = LoggerFactory.getLogger(TestBeanPostProcessor.class);

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        logger.info("Set the Bean Factory.");
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {

        logger.info("afterSingletonsInstantiated !!!!!!!!!");

    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        System.out.println("Get Test Service");
        beanFactory.getBean("testService");
        System.out.println("Get Test Service");


        logger.info("Before!,{}",beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        logger.info("After,{}",beanName);

        return bean;
    }
}
