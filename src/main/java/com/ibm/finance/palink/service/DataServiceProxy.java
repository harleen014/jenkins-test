package com.ibm.finance.palink.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ibm.finance.palink.dataaccess.entities.ConfigParams;
import com.ibm.finance.palink.dataaccess.entities.DashboardWsLog;
import com.ibm.finance.palink.dataaccess.entities.DomBuyGrpID;
import com.ibm.finance.palink.dataaccess.entities.HWDomBuyGrpID;
import com.ibm.finance.palink.dataaccess.entities.HWHistoricalData;
import com.ibm.finance.palink.dataaccess.entities.HWQuoteData;
import com.ibm.finance.palink.dataaccess.entities.SWHistoricalData;
import com.ibm.finance.palink.dataaccess.entities.SWQuoteData;

@FeignClient("ipat-data-service")
public interface DataServiceProxy {
	
	@RequestMapping(path = "/getConfigParams", method = RequestMethod.GET)
	public List<ConfigParams> getAllConfigParams();
	
	@RequestMapping(path = "/getDashboardData/getSWQuoteData/{quoteId}", method = RequestMethod.GET)
	public SWQuoteData getSWQuoteData(@PathVariable("quoteId") String quoteId);
	
	@RequestMapping(path = "/getDashboardData/getSWDomBuygGrpId", params = { "custName", "custNo" }, method = RequestMethod.GET)
	public DomBuyGrpID getSWDomBuygGrpId(@RequestParam("custName") String custName, @RequestParam("custNo") String custNo);
	
	@RequestMapping(path = "/getDashboardData/getHWQuoteData/{quoteId}", method = RequestMethod.GET)
	public HWQuoteData getHWQuoteData(@PathVariable("quoteId") String quoteId);

	@RequestMapping(path = "/getDashboardData/getHWDomBuygGrpId", params = { "custName", "custNo", "country" }, method = RequestMethod.GET)
	public HWDomBuyGrpID getHWDomBuygGrpId(@RequestParam("custName") String custName, @RequestParam("custNo") String custNo, @RequestParam("country") String countryCd);
	
	//Geo -specific Changes - start
	@RequestMapping(path = "/getDashboardData/getHWDomBuygGrpIdNA", params = { "custName", "custNo", "country" }, method = RequestMethod.GET)
	public HWDomBuyGrpID getHWDomBuygGrpIdNA(@RequestParam("custName") String custName, @RequestParam("custNo") String custNo, @RequestParam("country") String countryCd);
	
	@RequestMapping(path = "/getDashboardData/getHWDomBuygGrpIdJP", params = { "custName", "custNo", "country" }, method = RequestMethod.GET)
	public HWDomBuyGrpID getHWDomBuygGrpIdJP(@RequestParam("custName") String custName, @RequestParam("custNo") String custNo, @RequestParam("country") String countryCd);
	 
	@RequestMapping(path = "/getDashboardData/getHWHistoricalData/{colName}/{colValue}", method = RequestMethod.GET)
	public List<HWHistoricalData> getHWHistoricalData(@PathVariable("colName") String colName, @PathVariable("colValue") String colValue);
	
	@RequestMapping(path = "/getDashboardData/getHWHistoricalDataNA/{colName}/{colValue}", method = RequestMethod.GET)
	public List<HWHistoricalData> getHWHistoricalDataNA(@PathVariable("colName") String colName, @PathVariable("colValue") String colValue);
	
	@RequestMapping(path = "/getDashboardData/getHWHistoricalDataJP/{colName}/{colValue}", method = RequestMethod.GET)
	public List<HWHistoricalData> getHWHistoricalDataJP(@PathVariable("colName") String colName, @PathVariable("colValue") String colValue);
	//Geo-Specific Changes - end, Written By Harleen, Dated 11/Sept
	
	@RequestMapping(path = "/getDashboardData/getSWHistoricalData/{iDBGClntNum}", method = RequestMethod.GET)
	public List<SWHistoricalData> getSWHistoricalData(@PathVariable("iDBGClntNum") String iDBGClntNum);
	
	@RequestMapping(path = "/getDashboardData/saveDashboardData", method = RequestMethod.POST)
	public DashboardWsLog saveDashboardData(@RequestBody DashboardWsLog dashboardWsLog);
}
