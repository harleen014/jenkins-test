package com.ibm.finance.palink.common.utils;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Utils {
	
	protected static Logger logger = LoggerFactory.getLogger(Utils.class);
	
	public static java.sql.Timestamp getCurrentTimestamp() {
		Calendar cal = Calendar.getInstance();
		return new java.sql.Timestamp(cal.getTime().getTime());
	}	
}
