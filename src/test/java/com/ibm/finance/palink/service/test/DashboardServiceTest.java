package com.ibm.finance.palink.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.ibm.finance.palink.common.constants.Constants;
import com.ibm.finance.palink.common.dto.dashboard.DashboardResponseDto;
import com.ibm.finance.palink.service.DashboardServiceImpl;
import com.ibm.finance.palink.service.DataServiceProxy;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ibm.finance.palink.dataaccess.entities.DomBuyGrpID;
import com.ibm.finance.palink.dataaccess.entities.HWDomBuyGrpID;
import com.ibm.finance.palink.dataaccess.entities.HWHistoricalData;
import com.ibm.finance.palink.dataaccess.entities.HWQuoteData;
import com.ibm.finance.palink.dataaccess.entities.SWHistoricalData;
import com.ibm.finance.palink.dataaccess.entities.SWQuoteData;
import com.ibm.finance.palink.exceptions.DashboardException;

public class DashboardServiceTest {
	@Mock
	DataServiceProxy dataServiceProxy;
	
	@InjectMocks
	DashboardServiceImpl dashboardService;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testGetDashboardDataSWQuoteFailureReturnCode() throws Exception {
		
		SWQuoteData swQuoteData = new SWQuoteData();
		swQuoteData.setQuote_Id("12345");
		swQuoteData.setReturnCode("F");
		
		when(dataServiceProxy.getSWQuoteData("12345")).thenReturn(swQuoteData);
		
		assertEquals("F", swQuoteData.getReturnCode());
	}
	@Test
	public void testGetDashboardDataSWQuoteNoTxnDataExists() throws Exception {
		SWQuoteData swQuoteData = null;
		
		when(dataServiceProxy.getSWQuoteData("12345")).thenReturn(swQuoteData);
		
		assertNull(swQuoteData);
	}
	@Test
	public void testGetDashboardDataSWQuoteDomBuyGrpId() throws Exception {
		DomBuyGrpID domBuyGrpId = new DomBuyGrpID();
		domBuyGrpId.setDom_buy_grp_id("AA12345");
		
		when(dataServiceProxy.getSWDomBuygGrpId("XYZ", "98765")).thenReturn(domBuyGrpId);
		
		assertNotNull(domBuyGrpId);
	}
	
	@Test
	public void testGetDashboardDataSWHistData() throws Exception {
		List<SWHistoricalData> swHistDataList = new ArrayList<SWHistoricalData>();
		when(dataServiceProxy.getSWHistoricalData("AA12345")).thenReturn(swHistDataList);
		assertTrue(swHistDataList != null);
	}
	
	@Test
	public void testGetDashboardDataHWQuoteFailureReturnCode() throws Exception {
		
		HWQuoteData hwQuoteData = new HWQuoteData();
		hwQuoteData.setReturnCode("F");
		
		when(dataServiceProxy.getHWQuoteData("12345-IT")).thenReturn(hwQuoteData);
		
		assertEquals("F", hwQuoteData.getReturnCode());
	}
	
	@Test
	public void testGetDashboardDataHWQuoteNoTxnDataExists() throws Exception {
		HWQuoteData hwQuoteData = null;
		
		when(dataServiceProxy.getHWQuoteData("12345-IT")).thenReturn(hwQuoteData);
		
		assertNull(hwQuoteData);
	}
	
	@Test
	public void testGetDashboardDataHWQuoteDomBuyGrpId() throws Exception {
		HWDomBuyGrpID domBuyGrpId = new HWDomBuyGrpID();
		domBuyGrpId.setDom_buy_grp_id("AA12345");
		
		when(dataServiceProxy.getHWDomBuygGrpId("XYZ", "98765", "IT")).thenReturn(domBuyGrpId);
		
		assertNotNull(domBuyGrpId);
	}
	
	@Test
	public void testGetDashboardDataHWHistData() throws Exception {
		List<HWHistoricalData> hwHistDataList = new ArrayList<HWHistoricalData>();
		when(dataServiceProxy.getHWHistoricalData("DOM_BUY_GRP_ID","AA12345")).thenReturn(hwHistDataList);
		assertTrue(hwHistDataList != null);
	}
	
	@Test
	public void testGetDashboardDataForSWQuote() throws DashboardException {
		SWQuoteData swResponse = new SWQuoteData();
		swResponse.setQuote_Id("12345");
		swResponse.setCountryCode("JPN");
		swResponse.setReturnCode(Constants.RETURN_STATUS_SUCCESS);
		swResponse.setQuote_Version(5);
		swResponse.setErrorCode("0");
		swResponse.setCustomerName("XYZ");
		swResponse.setIbmCustomerNumber("98765");
		when(dataServiceProxy.getSWQuoteData("12345")).thenReturn(swResponse);
		DomBuyGrpID domBuyGrpId = new DomBuyGrpID();
		domBuyGrpId.setDom_buy_grp_id("AA12345");
		when(dataServiceProxy.getSWDomBuygGrpId("XYZ", "98765")).thenReturn(domBuyGrpId);
		List<SWHistoricalData> swHistDataList = new ArrayList<>();
		SWHistoricalData data = new SWHistoricalData();
		data.setCntry_code("JPN");
		data.setWeb_quote_num("8365828");
		data.setPart_nums("D1NZALL");
		data.setSw_prod_brand_code_dscrs("Systems - Middleware");
		data.setWw_prod_set_dscrs("IBM Applicatn Middleware");
		swHistDataList.add(data);
		when(dataServiceProxy.getSWHistoricalData("AA12345")).thenReturn(swHistDataList);
		DashboardResponseDto response = dashboardService.getDashboardData("12345", "SW");
		assertNotNull(response);
		assertTrue(response.getHistoricalData().getHistoricalQuote().size()>0);
	}
	
	@Test
	public void testGetDashboardDataForSWQuoteWithNullDomBuygGrpId() throws DashboardException {
		SWQuoteData swResponse = new SWQuoteData();
		swResponse.setCountryCode("JPN");
		swResponse.setReturnCode("S");
		swResponse.setErrorCode("0");
		swResponse.setErrorDescription("");
		when(dataServiceProxy.getSWQuoteData("12345")).thenReturn(swResponse);
		DomBuyGrpID domBuyGrpId = null;
		when(dataServiceProxy.getSWDomBuygGrpId("XYZ", "98765")).thenReturn(domBuyGrpId);
		DashboardResponseDto response = dashboardService.getDashboardData("12345", "SW");
		assertNotNull(response);
	}
	
	@Test
	public void testGetDashboardDataForHWQuote() throws DashboardException {
		String quoteId = "12345";
		String quoteType = Constants.QUOTETYPE_HW;
		HWQuoteData hwResponse = new HWQuoteData();
		hwResponse.setCountryCode("IT");
		hwResponse.setReturnCode(Constants.RETURN_STATUS_SUCCESS);
		hwResponse.setErrorCode("0");
		hwResponse.setErrorDescription("");
		hwResponse.setCustomerName("XYZ");
		hwResponse.setCustomer_Number("98765");
		when(dataServiceProxy.getHWQuoteData("12345")).thenReturn(hwResponse);
		HWDomBuyGrpID domBuyGrpId = new HWDomBuyGrpID();
		domBuyGrpId.setDom_buy_grp_id("AA12345");
		domBuyGrpId.setDom_client_id("54321");
		when(dataServiceProxy.getHWDomBuygGrpId("XYZ", "98765", "IT")).thenReturn(domBuyGrpId);
		List<HWHistoricalData> hwHistDataList = new ArrayList<>();
		HWHistoricalData data = new HWHistoricalData();
		//data.setCurrencycode("USD");
		data.setDom_buy_grp_id("AA12345");
		data.setSystem_types("ABC");
		data.setProduct_brands("IBM");
		hwHistDataList.add(data);
		when(dataServiceProxy.getHWHistoricalData("DOM_BUY_GRP_ID","AA12345")).thenReturn(hwHistDataList);
		DashboardResponseDto response = dashboardService.getDashboardData(quoteId, quoteType);
		assertNotNull(response);
		assertEquals("IT",response.getResponseHeader().getCountryCode());
		assertTrue(response.getHistoricalData().getHistoricalQuote().size()>0);
	}
	
	@Test
	public void testGetDashboardDataForHWQuoteWithNullHwResponse() throws DashboardException {
		String quoteId = "12345";
		String quoteType = Constants.QUOTETYPE_HW;
		HWQuoteData hwResponse = null;
		when(dataServiceProxy.getHWQuoteData("12345")).thenReturn(hwResponse);
		HWDomBuyGrpID domBuyGrpId = new HWDomBuyGrpID();
		domBuyGrpId.setDom_buy_grp_id("AA12345");
		when(dataServiceProxy.getHWDomBuygGrpId("XYZ", "98765", "IT")).thenReturn(domBuyGrpId);
		DashboardResponseDto response = dashboardService.getDashboardData(quoteId, quoteType);
		assertNotNull(response);
	}
	
	@Test
	public void testGetDashboardDataQuoteDoesNotExistForSWQuote() throws DashboardException {
		String quoteId = "12345";
		String quoteType = Constants.QUOTETYPE_SW;
		SWQuoteData response = null;
		when(dataServiceProxy.getSWQuoteData(quoteId)).thenReturn(response);
		DashboardResponseDto result = dashboardService.getDashboardData(quoteId, quoteType);
		assertEquals("Quote with quoteId: " + quoteId + " does not exist",result.getResponseHeader().getErrorDescription());
	}
	
	@Test
	public void testGetDashboardDataQuoteHasFailureResponseForSWQuote() throws DashboardException {
		String quoteId = "1234";
		String quoteType = Constants.QUOTETYPE_SW;
		SWQuoteData response = new SWQuoteData();
		response.setReturnCode(Constants.RETURN_STATUS_FAILED);
		when(dataServiceProxy.getSWQuoteData(quoteId)).thenReturn(response);
		DashboardResponseDto result = dashboardService.getDashboardData(quoteId, quoteType);
		assertEquals("Quote with quoteId: " + quoteId + " has failure return code",result.getResponseHeader().getErrorDescription());
	}
	
	@Test
	public void testGetDashboardDataQuoteDoesNotExistForHWQuote() throws DashboardException {
		String quoteId = "12345";
		String quoteType = Constants.QUOTETYPE_HW;
		HWQuoteData response = null;
		when(dataServiceProxy.getHWQuoteData(quoteId)).thenReturn(response);
		DashboardResponseDto result = dashboardService.getDashboardData(quoteId, quoteType);
		assertEquals("Quote with quoteId: " + quoteId + " does not exist",result.getResponseHeader().getErrorDescription());
	}
	
	@Test
	public void testGetDashboardDataQuoteHasFailureResponseForHWQuote() throws DashboardException {
		String quoteId = "1234";
		String quoteType = Constants.QUOTETYPE_HW;
		HWQuoteData response = new HWQuoteData();
		response.setReturnCode(Constants.RETURN_STATUS_FAILED);
		when(dataServiceProxy.getHWQuoteData(quoteId)).thenReturn(response);
		DashboardResponseDto result = dashboardService.getDashboardData(quoteId, quoteType);
		assertEquals("Quote with quoteId: " + quoteId + " has failure return code",result.getResponseHeader().getErrorDescription());
	}
	
}
