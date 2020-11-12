package com.learning.autoconfigure.aware;

import com.sun.istack.internal.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Wang Xu
 * @date 2020/10/19
 */
public class IApplicationContext implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public Object getBean(String beanName) {
        return this.applicationContext.getBean(beanName);
    }

    public <T> T getBean(String beanName, Class<T> clazz) {
        return this.applicationContext.getBean(beanName, clazz);
    }

    public <T> T getBean(Class<T> clazz) {
        return this.applicationContext.getBean(clazz);
    }
}