package com.ibm.finance.palink.monitoring;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BusinessProfiler {

	protected static Logger logger = LoggerFactory.getLogger(BusinessProfiler.class.getName());

	@Pointcut("execution(!static * com.ibm.finance.palink.service.*.*(..))")
	public void businessMethods() {
		//This methods points Pointcut
	}

	@Around("businessMethods()")
	public Object profile(ProceedingJoinPoint pjp) throws Throwable {
		try {
			long start = System.currentTimeMillis();

			Object output = pjp.proceed();

			Long elapsedTime = System.currentTimeMillis() - start;
			MDC.put("API", pjp.getSignature().toLongString());
			MDC.put("executionTime", String.valueOf(elapsedTime));
			logger.info("Method execution time for API {} : {} milliseconds.",pjp.getSignature(),elapsedTime);			
			return output;
		} finally {
			MDC.clear();
		}
	}

}