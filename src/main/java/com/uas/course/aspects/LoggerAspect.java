package com.uas.course.aspects;

import java.time.Duration;
import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

	@Around("execution(* com.uas.course.*.*.*(..))")
	public Object logTime(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info(joinPoint.getSignature().toLongString() + "execution starts!");
		Instant start = Instant.now();

		Object returnObject = joinPoint.proceed();

		Instant end = Instant.now();
		long timeTaken = Duration.between(start, end).toMillis();
		log.info("time taken " + timeTaken);
		log.info(joinPoint.getSignature().toString() + " execution ends!");

		return returnObject;
	}

}
