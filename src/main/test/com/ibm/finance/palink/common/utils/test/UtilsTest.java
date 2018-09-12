package com.ibm.finance.palink.common.utils.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.ibm.finance.palink.common.utils.Utils;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

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

	@Test
	public void testIsZeroSuccess() {
		boolean result = Utils.isZero(0);
		assertTrue(result);
	}

	@Test
	public void testIsZeroFailure() {
		boolean result = Utils.isZero(5L);
		assertFalse(result);
	}

	@Test
	public void testParseToGetSQLDateSuccess() throws ParseException {
		java.sql.Timestamp ts = Utils.parseToGetSQLDate("12-Jan-2018", "dd-MMM-yyyy");
		assertNotNull(ts);
	}

	@Test
	public void testParseToGetSQLDateFailure() {
		java.sql.Timestamp ts = Utils.parseToGetSQLDate("12/Jan/2018", "dd-MMM-yyyy");
		assertNull(ts);
	}
}
