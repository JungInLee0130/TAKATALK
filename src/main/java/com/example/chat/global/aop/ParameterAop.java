package com.example.chat.global.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect // AOP를 정의하는 클래스를 지칭함.
@Component
public class ParameterAop {

    // com.example.chat.login.controller 하위클래스 모두 적용. (연습)
    @Pointcut("execution(* com.example.chat..controller..*.*(..)) ||" +
            "execution(* com.example.chat..service..*.*(..))")
    private void cut() {

    }

    @Before("cut()")
    public void before(JoinPoint joinPoint) {
        // 실행되고있는 함수 이름을 가져오고 출력
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        System.out.println("----------" + method.getName() + " 실행" + "-----------");

        // 파라미터 이름
        final String[] parameterNames = methodSignature.getParameterNames();
        // 메서드에 들어가는 매개변수 배열(value)을 읽어옴.
        final Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length; i++) {
            if (args[i] != null) {
                System.out.print("type : " + args[i].getClass().getSimpleName());
                System.out.print(", parameterName : " + parameterNames[i]);
                System.out.println(", value : " + args[i]);
            } else {
                System.out.print("type : null");
                System.out.print(", parameterName : " + parameterNames[i]);
                System.out.println(", value : null");
            }
        }
    }

    @AfterReturning(value = "cut()", returning = "obj")
    public void afterReturn(JoinPoint joinPoint, Object obj) {
        if (obj != null) {
            // Hibernate 프록시 객체이면서 초기화가 안된 상태라면
            if (!Hibernate.isInitialized(obj)) {
                System.out.println("return (Proxy Object - Not Initialized) : " + obj.getClass().getName());
            } else {
                System.out.println("return " + obj);
            }

        }
        System.out.println("-----------------------------------------");
    }
}
