package com.ibm.finance.palink.common.utils.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.ibm.finance.palink.common.utils.Utils;

public class UtilsTest {
Utils utils;
	
	@Before
	public void setup() {
		utils = new Utils();
	}

	@Test
	public void testGetCurrentTimestamp() {
		@SuppressWarnings("static-access")
		java.sql.Timestamp ts = utils.getCurrentTimestamp();
		assertNotNull(ts);
	}
}
