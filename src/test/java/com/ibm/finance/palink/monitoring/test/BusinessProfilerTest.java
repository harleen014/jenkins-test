package com.ibm.finance.palink.monitoring.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ibm.finance.palink.monitoring.BusinessProfiler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class BusinessProfilerTest {
	@Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Mock
    private ProceedingJoinPoint pjp;
	
	BusinessProfiler businessProfiler;
	
	@Before
	public void setup() {
		businessProfiler = new BusinessProfiler();
	}
	
	@Test
	public void testProfile() throws Throwable {
		MethodSignature signature = mock(MethodSignature.class);
		when(pjp.getSignature()).thenReturn(signature);
		Object o = businessProfiler.profile(pjp);
		verify(pjp, times(1)).proceed();
		System.out.println(o);
	}
}
