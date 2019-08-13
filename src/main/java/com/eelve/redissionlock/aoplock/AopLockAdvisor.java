package com.eelve.redissionlock.aoplock;

import com.eelve.redissionlock.distributedlock.annotation.Lock;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
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
