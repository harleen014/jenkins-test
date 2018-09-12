package com.ibm.finance.palink.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.finance.palink.common.dto.dashboard.DashboardResponseDto;
import com.ibm.finance.palink.exceptions.DashboardException;
import com.ibm.finance.palink.service.DashboardService;

@RestController
public class DashboardController {
	
	protected static Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private DashboardService service;

	@RequestMapping(path = "/getDashboard/{quoteId}/{quoteType}", method = RequestMethod.GET)
	public DashboardResponseDto getDashboardData(@PathVariable("quoteId") String quoteId, @PathVariable("quoteType") String quoteType) throws DashboardException {
		
		logger.info("API called /getDashboardData/{}/{}", quoteId, quoteType);
		try {
			return service.getDashboardData(quoteId, quoteType);
		} catch(DashboardException e) {
			logger.error("Exception in /getDashboardData/{}/{}", quoteId, quoteType);
			throw e;
		}
		
	}

}