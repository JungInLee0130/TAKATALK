package com.example.chat.global.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
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
        // 매개변수 배열의 종류와 값을 출력
        /*for (Object obj : args) {
            if (obj == null) {
                System.out.println("type : null");
                System.out.println("value : null");
                continue;
            }
            System.out.println("type : " + obj.getClass().getSimpleName());
            System.out.println("value : " + obj);
        }*/
    }

    @AfterReturning(value = "cut()", returning = "obj")
    public void afterReturn(JoinPoint joinPoint, Object obj) {
        System.out.println("return " + obj);
        System.out.println("-----------------------------------------");
    }
}
