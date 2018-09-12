package com.ibm.finance.palink.controller.test;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ibm.finance.palink.controller.DashboardController;
import com.ibm.finance.palink.exceptions.DashboardException;
import com.ibm.finance.palink.common.dto.dashboard.DashboardResponseDto;
import com.ibm.finance.palink.service.DashboardService;
import com.ibm.finance.palink.DashboardServiceApplication;
import com.ibm.finance.palink.common.dto.dashboard.ResponseHeaderDto;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = DashboardServiceApplication.class)
//@SpringBootTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DashboardControllerTest {
private MockMvc mockMvc;
	
	@Mock
	DashboardService dashboardService;
	
	@InjectMocks
	DashboardController dashboardController;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);       
        this.mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();
	}
	
	@Ignore
	public void testGetDashboardData() throws Exception {
		DashboardResponseDto responseDto = new DashboardResponseDto();
		ResponseHeaderDto header = new ResponseHeaderDto();
		header.setQuoteID("12345");
		header.setQuoteType("SW");
		responseDto.setResponseHeader(header);
		when(dashboardService.getDashboardData("12345", "SW")).thenReturn(responseDto);
		mockMvc.perform(get("/getDashboardData/12345/SW")).andExpect(status().isOk());
	}
	
	@SuppressWarnings("unchecked")
	@Ignore
	public void testGetDashboardDataFailure() throws Exception {
		when(dashboardService.getDashboardData("12345", "SW")).thenThrow(DashboardException.class);
		mockMvc.perform(get("/getDashboardData/12345/SW")).andExpect(status().isNotFound());
	}
}
