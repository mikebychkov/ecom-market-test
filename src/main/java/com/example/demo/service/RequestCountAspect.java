package com.example.demo.service;

import com.example.demo.annotations.ControlRequestCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Log4j2
@RequiredArgsConstructor
public class RequestCountAspect {

    private final RequestCountService requestCountService;

    @Pointcut("@annotation(com.example.demo.annotations.ControlRequestCount)")
    private void controlRequestCount() {}

    @Around("controlRequestCount()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String ip = request.getHeader("X-Forwarded-For");
        String aValue = getAnnotatedValue(joinPoint);

        log.info("IP: {} {}", ip, aValue);

        requestCountService.registerRequest(ip, aValue);

        return joinPoint.proceed();
    }

    private String getAnnotatedValue(ProceedingJoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        ControlRequestCount ann = methodSignature.getMethod().getAnnotation(ControlRequestCount.class);

        return ann.value();
    }
}
