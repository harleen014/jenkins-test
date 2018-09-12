package com.ibm.finance.palink.service;

import com.ibm.finance.palink.common.dto.dashboard.DashboardResponseDto;
import com.ibm.finance.palink.exceptions.DashboardException;

public interface DashboardService {

	public DashboardResponseDto getDashboardData(String quoteId, String quoteType) throws DashboardException;

}
