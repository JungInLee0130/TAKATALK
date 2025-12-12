package com.example.chat.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class TimerAop {
    @Pointcut("execution(* com.example.chat..controller..*.*(..)) ||" +
            "execution(* com.example.chat..service..*.*(..))")
    private void cut() {

    }

    @Pointcut("@annotation(com.example.chat.global.aop.annotation.Timer)")
    private void enableTimer(){

    }

    // 어노테이션 적용법 : &&로 연결
    @Around("cut() && enableTimer()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메서드 시작전
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 메서드가 실행되는 지점
        Object result = joinPoint.proceed();

        // 메서드 종료 후
        stopWatch.stop();

        System.out.println("총 걸린 시간 : " + stopWatch.getTotalTimeSeconds() + " seconds");

        return result;
    }
}
