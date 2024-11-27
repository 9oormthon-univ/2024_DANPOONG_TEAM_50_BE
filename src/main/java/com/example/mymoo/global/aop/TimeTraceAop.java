package com.example.mymoo.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
public class TimeTraceAop {

    private final Logger log = LoggerFactory.getLogger(TimeTraceAop.class);

    @Around("@within(com.example.mymoo.global.aop.LogExecutionTime)")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(joinPoint.getSignature().toShortString()); // 태스크 이름 설정 후 측정 시작

        Object proceed = joinPoint.proceed(); // 프로세스 진행
        stopWatch.stop(); //프로세스 종료 측정 종료

        log.info("Execution time for {}: {} ns", joinPoint.getSignature().toShortString(), stopWatch.getLastTaskTimeNanos());
        return proceed;
    }

}
