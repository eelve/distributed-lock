package com.eelve.redissionlock.aoplock;

import com.eelve.redissionlock.distributedlock.DistributedLocker;
import com.eelve.redissionlock.distributedlock.annotation.Lock;
import org.aopalliance.intercept.MethodInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description Created by zeng.yubo on 2019/8/12.
 */
public class AopLockAdvisor extends StaticMethodMatcherPointcutAdvisor {

    public AopLockAdvisor(DistributedLocker distributedLocker){
        setAdvice((MethodInterceptor) methodObj -> {
            Method method = methodObj.getMethod();
            Lock lockAnn = findMethodAnnotation(method.getDeclaringClass(),method,Lock.class);
            String lockName = lockAnn.value();
            int lessTime = lockAnn.leaseTime();
            if(StringUtils.isEmpty(lockName)){
                lockName = method.getName();
            }
            if(lessTime<0){
                lessTime = 30;
            }
            distributedLocker.lock(lockName,lessTime);
            System.out.println("上锁");
            try {
                return methodObj.proceed();
            } finally {
                //如果该线程还持有该锁，那么释放该锁。如果该线程不持有该锁，说明该线程的锁已到过期时间，自动释放锁
                System.out.println("释放");
                if (distributedLocker.isHeldByCurrentThread(lockName)) {
                    distributedLocker.unlock(lockName);
                }
            }
        });
    }

    //匹配到的方法加入到AOP中，织入
    @Override
    public boolean matches(Method method, Class<?> aClass) {
        Lock lock = findMethodAnnotation(aClass, method, Lock.class);
        if (null != lock) {
            return true;
        }else {
            return false;
        }
    }


    private <T extends Annotation> T findMethodAnnotation(Class<?> targetClass, Method method, Class<T> annClass) {
        Method m = method;
        T a = AnnotationUtils.findAnnotation(m,annClass);
        if (a != null) {
            return a;
        }
        //获取代理方法的真实方法
        m = AopUtils.getMostSpecificMethod(m, targetClass);
        //真实方法上获取注解
        a = AnnotationUtils.findAnnotation(m, annClass);
        if (a == null) {
            List<Class> supers = new ArrayList<>();
            supers.addAll(Arrays.asList(targetClass.getInterfaces()));
            if (targetClass.getSuperclass() != Object.class) {
                supers.add(targetClass.getSuperclass());
            }

            for (Class aClass : supers) {
                if(aClass==null){
                    continue;
                }
                Method ims[] = new Method[1];

                //doWithLocalMethods 针对指定类型上的所有方法，依次调用MethodCallback回调
                //doWithMethods 是 doWithLocalMethods 的增强 ，该方法会递归向上查询所有父类和实现的接口上的所有方法并处理
                ReflectionUtils.doWithMethods(aClass, im -> {
                    if (im.getName().equals(method.getName()) && im.getParameterCount() == method.getParameterCount()) {
                        ims[0] = im;
                    }
                });

                if (ims[0] != null) {
                    a = findMethodAnnotation(aClass, ims[0], annClass);
                    if (a != null) {
                        return a;
                    }
                }
            }
        }
        return a;
    }
}
