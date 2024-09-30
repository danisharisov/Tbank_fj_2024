package com.example.fj_2024_lesson_5.timed;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class TimedPostProcessor implements BeanPostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TimedPostProcessor.class);
    private final Map<String, Class<?>> beanClasses = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Timed.class)) {
            beanClasses.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Timed.class) || Arrays.stream(beanClass.getMethods()).anyMatch(method -> method.isAnnotationPresent(Timed.class))) {
            return createProxy(bean, beanClass);
        }
        return bean;
    }

    private Object createProxy(Object target, Class<?> targetClass) {
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimedInterceptor(targetClass));
        return proxyFactory.getProxy();
    }

    private static class TimedInterceptor implements MethodInterceptor {
        private final Class<?> targetClass;

        public TimedInterceptor(Class<?> targetClass) {
            this.targetClass = targetClass;
        }
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            boolean isClassTimed = targetClass.isAnnotationPresent(Timed.class);
            boolean isMethodTimed = invocation.getMethod().isAnnotationPresent(Timed.class);
            if (isClassTimed || isMethodTimed) {
                String methodName = invocation.getMethod().getName();
                String className = targetClass.getSimpleName();
                logger.info("Method [{}] in class [{}] started...", methodName, className);
                Instant start = Instant.now();
                try {
                    return invocation.proceed();
                } finally {
                    Duration duration = Duration.between(start, Instant.now());
                    logger.info("Method [{}] finished in [{}] ms.", methodName, duration.toMillis());
                }
            }
            return invocation.proceed();
        }
    }
}