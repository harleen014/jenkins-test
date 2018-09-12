package com.ibm.finance.palink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.finance.palink.common.constants.Constants;
import com.ibm.finance.palink.common.dto.dashboard.CustomerInformationDto;
import com.ibm.finance.palink.common.dto.dashboard.DashboardResponseDto;
import com.ibm.finance.palink.common.dto.dashboard.HistoricalDataDto;
import com.ibm.finance.palink.common.dto.dashboard.HistoricalQuoteDto;
import com.ibm.finance.palink.common.dto.dashboard.QuoteInformationDto;
import com.ibm.finance.palink.common.dto.dashboard.ResponseHeaderDto;
import com.ibm.finance.palink.common.utils.Utils;
import com.ibm.finance.palink.dataaccess.entities.DashboardWsLog;
import com.ibm.finance.palink.dataaccess.entities.DomBuyGrpID;
import com.ibm.finance.palink.dataaccess.entities.HWDomBuyGrpID;
import com.ibm.finance.palink.dataaccess.entities.HWHistoricalData;
import com.ibm.finance.palink.dataaccess.entities.HWQuoteData;
import com.ibm.finance.palink.dataaccess.entities.SWHistoricalData;
import com.ibm.finance.palink.dataaccess.entities.SWQuoteData;
import com.ibm.finance.palink.exceptions.DashboardException;

@Service
public class DashboardServiceImpl implements DashboardService {

	protected static Logger logger = LoggerFactory.getLogger(DashboardServiceImpl.class);
	protected JAXBContext jc = null;
	
	@Autowired
	private DataServiceProxy dataService;

	@Override
	public DashboardResponseDto getDashboardData(String quoteId, String quoteType) throws DashboardException{
		DashboardResponseDto response = null;
		if(quoteType != null) {
			if((!quoteType.trim().equals(Constants.QUOTETYPE_HW))) {
				response = getSWDashboardData(quoteId, quoteType);
			} else if(quoteType.trim().equals(Constants.QUOTETYPE_HW)) {
				response =  getHWDashboardData(quoteId , quoteType);
			}
		}
		return response;
	}
	
	private DashboardResponseDto getSWDashboardData(String quoteId , String quoteType) {
		logger.info("Start");
		
		DashboardResponseDto dashboardResponse = new DashboardResponseDto();
		ResponseHeaderDto header = new ResponseHeaderDto();
		QuoteInformationDto quoteInfo = new QuoteInformationDto();
		
		String IDBGClntNum = Constants.RETURN_UNASSIGN;
		String colName = "";
		String colValue="";
		
		try {
			SWQuoteData response = dataService.getSWQuoteData(quoteId);

			if(null == response) {
				logger.info("No transaction data found for quote: {}",quoteId);
				return getQuoteDoesNotExistDashboardResponse(quoteId);
			} else {
				
				if (Constants.RETURN_STATUS_FAILED.equals(response.getReturnCode())) { 
					logger.info("Quote {} has failure status in transaction data",quoteId);
					return getQuoteHasFailureStatusResponse(quoteId);
				}
				logger.info("Quote {} has transaction data!!",quoteId);
				
				header.setReturnCode(response.getReturnCode());
				header.setRequestingApplicationId(Constants.RETURN_APPLICATION_ID_DASHBOARD);
				header.setQuoteID(quoteId);
				header.setQuoteType(quoteType);
				header.setVersion(response.getQuote_Version() == null ? 0 : response.getQuote_Version());
				header.setCountryCode(response.getCountryCode());
				header.setErrorCode(Short.valueOf(response.getErrorCode()));
				
				header.setErrorDescription(response.getErrorDescription());
				header.setQuoteType(Constants.QUOTETYPE_SW);
				
				dashboardResponse.setResponseHeader(header);
				
				CustomerInformationDto custDto  = new CustomerInformationDto();
				custDto.setCustomerName(response.getCustomerName());
				custDto.setIbmCustomerNumber(response.getIbmCustomerNumber());
				quoteInfo.setCustomerInformation(custDto);
				dashboardResponse.setQuoteInformation(quoteInfo);
				
				//get domestice buying grp id for SW
				DomBuyGrpID domBuyGrpId = dataService.getSWDomBuygGrpId(custDto.getCustomerName(), custDto.getIbmCustomerNumber());
				IDBGClntNum = domBuyGrpId.getDom_buy_grp_id();
				
				if(IDBGClntNum == null || IDBGClntNum.trim().length() <= 0 || IDBGClntNum.equals(Constants.RETURN_UNASSIGN))
					return dashboardResponse;
				
				if(IDBGClntNum != null && !IDBGClntNum.trim().isEmpty() && !IDBGClntNum.equalsIgnoreCase(Constants.RETURN_UNASSIGN)) {
					colName = "DOM_BUY_GRP_ID";
					colValue = IDBGClntNum;
					quoteInfo.getCustomerInformation().setIbmDomstcBuyGrpClntNum(IDBGClntNum);	
				}
			}
			//get SW Historical Data
			setSWHistoricalData(dashboardResponse, IDBGClntNum);
			//get HW Historical Data
			setHwHistoricalData(dashboardResponse, colName, colValue);
			
		} catch(Exception e) {
			logger.error("Exception in getSWDashboardData method {}", e);
		}
		setDashboardWsLog(dashboardResponse);	
		return dashboardResponse;
	}
	
	private DashboardResponseDto getHWDashboardData(String quoteId , String quoteType) {
		logger.info("Start");
		
		DashboardResponseDto dashboardResponse = new DashboardResponseDto();
		ResponseHeaderDto header = new ResponseHeaderDto();
		
		String IDBGClntNum = Constants.RETURN_UNASSIGN;
		String domClientId = "";
		
		String colName = "";
		String colValue="";
		try {
			HWQuoteData hwQuoteData = dataService.getHWQuoteData(quoteId);
			if(null == hwQuoteData) {
				logger.info("No transaction data found for quote: {}", quoteId);
				return getQuoteDoesNotExistDashboardResponse(quoteId);
			} else {
				String returnCode = hwQuoteData.getReturnCode();
				
				if (Constants.RETURN_STATUS_FAILED.equals(returnCode.trim())) {
					logger.info("Quote {} has failure status in transaction data ", quoteId);
					return getQuoteHasFailureStatusResponse(quoteId);
				}
				logger.info("Quote {} has transaction data!!", quoteId);
				
				header.setReturnCode(returnCode);
				header.setRequestingApplicationId(Constants.RETURN_UNASSIGN);
				header.setQuoteID(quoteId);
				header.setQuoteType(quoteType);
				header.setVersion(hwQuoteData.getQuote_version()==null?0:hwQuoteData.getQuote_version());
				
				header.setCountryCode(hwQuoteData.getCountryCode());
				header.setReturnCode("S");
				header.setErrorCode((short) Constants.RETURN_SUCCESS );
				header.setErrorDescription(""); 
				dashboardResponse.setResponseHeader(header);				
				
				//get customer information as per geo specific
				HWDomBuyGrpID hwCustData = getHWCustomerData(hwQuoteData, dashboardResponse);
				if(hwCustData == null) {
					logger.info("Quote {} customer does not have buying grp id or client id mapping ", quoteId);
					dashboardResponse.getQuoteInformation().getCustomerInformation().setIbmDomstcBuyGrpClntNum(IDBGClntNum);
					dashboardResponse.getQuoteInformation().getCustomerInformation().setDomClientId(domClientId);
					return dashboardResponse;
				} else {
					IDBGClntNum = hwCustData.getDom_buy_grp_id();
					domClientId = hwCustData.getDom_client_id();
				
					logger.info("IDBGClntNum: {}",IDBGClntNum);
					logger.info("domClientId: {}",domClientId);
					
					dashboardResponse.getQuoteInformation().getCustomerInformation().setIbmDomstcBuyGrpClntNum(IDBGClntNum);
					dashboardResponse.getQuoteInformation().getCustomerInformation().setDomClientId(domClientId);
					
					if(IDBGClntNum != null && !IDBGClntNum.trim().isEmpty() && !IDBGClntNum.equalsIgnoreCase(Constants.RETURN_UNASSIGN)) {
						colName = "DOM_BUY_GRP_ID";
						colValue = IDBGClntNum;
					} else if(domClientId != null && !domClientId.trim().isEmpty() && !domClientId.equalsIgnoreCase(Constants.RETURN_UNASSIGN)) {
						colName = "DOM_CLIENT_ID";
						colValue = domClientId;	
					} else {
						return dashboardResponse;
					}
				}
			}
			//get SW Historical Data
			setSWHistoricalData(dashboardResponse, IDBGClntNum);
			//get HW Historical Data
			setHwHistoricalData(dashboardResponse,colName,colValue);
		} catch(Exception e) {
			logger.error("Exception in getHWDashboardData method {}", e);
		}
		setDashboardWsLog(dashboardResponse);
		return dashboardResponse;
	}
	
	private void setSWHistoricalData(DashboardResponseDto dashboardResponse,String IDBGClntNum) {
		//get SW historical data based on domestic buying group id 
		if (IDBGClntNum != null && !Constants.RETURN_UNASSIGN.equals(IDBGClntNum)) {
			List<SWHistoricalData> swHistDataList = dataService.getSWHistoricalData(IDBGClntNum);
			if(swHistDataList != null && swHistDataList.size() > 0) {
				logger.info("SW historical quotes found for customer:{}", swHistDataList.size());
				setSWHistoricalQuoteDTO(swHistDataList, dashboardResponse);
			}
		}
	}
	
	private void setHwHistoricalData(DashboardResponseDto dashboardResponse, String colName, String colValue) {
		boolean isNA = false;
		boolean isJP = false;
		
		isNA = isNACustomer(dashboardResponse.getResponseHeader().getCountryCode());
		isJP = isJPCustomer(dashboardResponse.getResponseHeader().getCountryCode());
		
		//get HW historical data based on domestic buying group id
		//Geo Specific Chnages -- for HW Historical Data - start 
		List<HWHistoricalData> hwHistDataList = null;
		if(isNA) {
			hwHistDataList = dataService.getHWHistoricalDataNA(colName, colValue);
		}
		else if(isJP){
			hwHistDataList = dataService.getHWHistoricalDataJP(colName, colValue);
		}
		else {
			hwHistDataList = dataService.getHWHistoricalData(colName, colValue);
		}
		//Geo Specific Chnages -- for HW Historical Data - End
		if(hwHistDataList != null && !hwHistDataList.isEmpty()) {
			logger.info("HW historical quotes found for customer:{}", hwHistDataList.size());
			
			setHWHistoricalQuoteDTO(hwHistDataList, dashboardResponse);
		}
	}
	
	private HWDomBuyGrpID getHWCustomerData(HWQuoteData hwQuoteData, DashboardResponseDto dashboardResponse) {
		boolean isNA = false;
		boolean isJP = false;
		QuoteInformationDto quoteInfo = new QuoteInformationDto();
		CustomerInformationDto custDto  = new CustomerInformationDto();
		custDto.setCustomerName(hwQuoteData.getCustomerName());
		custDto.setIbmCustomerNumber(hwQuoteData.getCustomer_Number());
		quoteInfo.setCustomerInformation(custDto);
		dashboardResponse.setQuoteInformation(quoteInfo);
		//Geo -specific chnages - start
		isNA = isNACustomer(dashboardResponse.getResponseHeader().getCountryCode());
		isJP = isJPCustomer(dashboardResponse.getResponseHeader().getCountryCode());
		//Geo - specific chnanges - end
		
		//get domestice buying grp id or client id for HW
		if(isNA) {
			 return dataService.getHWDomBuygGrpIdNA(custDto.getCustomerName(), custDto.getIbmCustomerNumber(), dashboardResponse.getResponseHeader().getCountryCode());
		}
		else if(isJP){
			return dataService.getHWDomBuygGrpIdJP(custDto.getCustomerName(), custDto.getIbmCustomerNumber(), dashboardResponse.getResponseHeader().getCountryCode());
		}
		return dataService.getHWDomBuygGrpId(custDto.getCustomerName(), custDto.getIbmCustomerNumber(), dashboardResponse.getResponseHeader().getCountryCode());
	}
	
	
	
	private void setDashboardWsLog(DashboardResponseDto dashboardResponse) {
		logger.info("Logging data in log table: Start");
		DashboardWsLog dashboardWSLog = new DashboardWsLog();
		dashboardWSLog.setQuoteId(dashboardResponse.getResponseHeader().getQuoteID());
		dashboardWSLog.setReceivedDate(Utils.getCurrentTimestamp());
		
		//get dashboard data
		if(dashboardResponse != null) {
			dashboardWSLog.setResponseString(objToXMLToString(dashboardResponse));
			dashboardWSLog.setReturnCode(dashboardResponse.getResponseHeader() == null ? "-":dashboardResponse.getResponseHeader().getReturnCode());
			dashboardWSLog.setErrorCode(dashboardResponse.getResponseHeader().getErrorCode() == null ? -1 : dashboardResponse.getResponseHeader().getErrorCode());
			dashboardWSLog.setErrorDescription(dashboardResponse.getResponseHeader().getErrorDescription() == null ? "-" : dashboardResponse.getResponseHeader().getErrorDescription());
		}
		dashboardWSLog.setCreated(Utils.getCurrentTimestamp());
		dashboardWSLog.setRespondedDate(Utils.getCurrentTimestamp());
		
		dataService.saveDashboardData(dashboardWSLog);
		logger.info("Logging data in log table: End");
	}
	
	private void setHWHistoricalQuoteDTO(List<HWHistoricalData> hwHistDataList, DashboardResponseDto response) {
		HistoricalDataDto historicalData = new HistoricalDataDto();
		
		
		List<HistoricalQuoteDto> historicalQuotelist = response.getHistoricalData().getHistoricalQuote();
		if(historicalQuotelist == null || historicalQuotelist.isEmpty()) {
			historicalQuotelist = new ArrayList<>();
		}
		
		Integer count = !historicalQuotelist.isEmpty() ? historicalQuotelist.size()  : 0;
		
		for (HWHistoricalData hwHistData : hwHistDataList) {
			
			count++;
			logger.info("----------------------------- HW historicalQuote count: {}", count);
			HistoricalQuoteDto historicalQuote = new HistoricalQuoteDto();
			
			historicalQuote.setHistoricalQuoteId(count);
			historicalQuote.setQuoteType(Constants.QUOTETYPE_HW);
			historicalQuote.setQuoteId(hwHistData.getQuote_Id());
			historicalQuote.setSysType(Arrays.asList((hwHistData.getSystem_types()).split(",")));
			historicalQuote.setProductBrand(Arrays.asList((hwHistData.getProduct_brands()).split(",")));
			
			historicalQuote.setSumList(hwHistData.getExtended_com_listprices());
			historicalQuote.setDealSize(hwHistData.getDealsize());
			historicalQuote.setDiscount(hwHistData.getQuote_lvl_dsc());
			historicalQuote.setQuoteDate(hwHistData.getQuote_date());
			historicalQuote.setCmrSectorName(hwHistData.getCmrsectorname());
			historicalQuote.setCmrIsUname(hwHistData.getCmrisuname());
			historicalQuote.setDomBuyGrpId(hwHistData.getDom_buy_grp_id());
			response.getQuoteInformation().getCustomerInformation().setIbmDomstcBuyGrpClntNum(historicalQuote.getDomBuyGrpId());
			historicalQuote.setValueSeller(hwHistData.getValueseller());
			historicalQuote.setCountryCode(hwHistData.getQuote_iso_2_ctry_code());
			historicalQuote.setWinLoss(hwHistData.getWin_ind());
			historicalQuote.setUpgMes(hwHistData.getUpd_flag());
			historicalQuote.setDomBuyGrpName(hwHistData.getDom_buy_grp_name());
			response.getQuoteInformation().getCustomerInformation().setIbmDomstcBuyGrpClntName(historicalQuote.getDomBuyGrpName());
			historicalQuote.setIndirect(hwHistData.getIndirect());
			historicalQuote.setOpportunityId(hwHistData.getOpportunityid());
			historicalQuotelist.add(historicalQuote);
		}	
		historicalData.setHistoricalQuote(historicalQuotelist);
		response.setHistoricalData(historicalData);
		
	}

	private boolean isNACustomer(String countryCd) {
		boolean result = false;
		if(countryCd.equalsIgnoreCase("US") || countryCd.equalsIgnoreCase("CA"))
			result = true;
		return result ;
	}
	
	private boolean isJPCustomer(String countryCd) {
		boolean result = false;
		if(countryCd.equalsIgnoreCase("JP"))
			result = true;
		return result ;
	}
	private void setSWHistoricalQuoteDTO(List<SWHistoricalData> swHistDataList, DashboardResponseDto response) {
		HistoricalDataDto historicalData = response.getHistoricalData() ;
		if(historicalData == null)
			historicalData = new HistoricalDataDto();
		
		List<HistoricalQuoteDto> historicalQuotelist = historicalData.getHistoricalQuote() ;
		if( historicalQuotelist == null)
			historicalQuotelist = new ArrayList<>();
		
		HistoricalQuoteDto historicalQuote = null;
		
		Integer count = !historicalQuotelist.isEmpty() ? historicalQuotelist.size()  : 0;
		
		for (SWHistoricalData swHistData : swHistDataList) {
			historicalQuote = new HistoricalQuoteDto();
			
			count++;
			logger.info("-----------------------------SW historicalQuote count: {}", count);
			
			historicalQuote.setHistoricalQuoteId(count);
			historicalQuote.setQuoteType(Constants.QUOTETYPE_SW);
			historicalQuote.setWebQuoteNum(Integer.valueOf(swHistData.getWeb_quote_num()));
			historicalQuote.setPartNum(Arrays.asList((swHistData.getPart_nums()).split(",")));
			historicalQuote.setSumEntitled(swHistData.getEntited());
			historicalQuote.setSumQuoted(swHistData.getQuoted());
			historicalQuote.setDiscount(swHistData.getBid_discount());
			historicalQuote.setSubmitDate(swHistData.getApproval_date());
			historicalQuote.setSwProdBrandCodeDscr(Arrays.asList((swHistData.getSw_prod_brand_code_dscrs()).split(",")));
			historicalQuote.setWwProdSetDscr(Arrays.asList((swHistData.getWw_prod_set_dscrs()).split(",")));
			historicalQuote.setCntryCode(swHistData.getCntry_code());
			historicalQuote.setWebQuoteCurrencyCode(swHistData.getWeb_quote_currency_code());
			historicalQuote.setWin(swHistData.getOrdered());
			historicalQuote.setIbmDomstcBuyGrpClntName(swHistData.getIbm_domstc_buyg_grp_clnt_name());
			historicalQuote.setFulfillment(swHistData.getFulfillment_src());
			historicalQuote.setOpportunityId(swHistData.getOpprtnty_num());
			response.getQuoteInformation().getCustomerInformation().setIbmDomstcBuyGrpClntName(historicalQuote.getIbmDomstcBuyGrpClntName());
			
			historicalQuotelist.add(historicalQuote);
		}
		historicalData.setHistoricalQuote(historicalQuotelist);
		
		response.setHistoricalData(historicalData);
		
	}
	private DashboardResponseDto getQuoteDoesNotExistDashboardResponse(String quoteId) {
		DashboardResponseDto dto = new DashboardResponseDto();
		
		ResponseHeaderDto header = new ResponseHeaderDto();
		header.setQuoteID(quoteId);
		header.setErrorCode(Constants.ERROR_CODE_QUOTEID_DOES_NOT_EXIST);
		header.setErrorDescription("Quote with quoteId: " + quoteId + " does not exist");
		header.setRequestingApplicationId(Constants.RETURN_APPLICATION_ID_DASHBOARD);
		header.setReturnCode("F");
		dto.setResponseHeader(header);
		
		return dto;
	}
	
	private DashboardResponseDto getQuoteHasFailureStatusResponse(String quoteId) {
		DashboardResponseDto dto = new DashboardResponseDto();
		
		ResponseHeaderDto header = new ResponseHeaderDto();
		header.setQuoteID(quoteId);
		header.setErrorCode(Constants.ERROR_CODE_QUOTEID_HAS_FAILURE_RETURNCODE);
		header.setErrorDescription("Quote with quoteId: " + quoteId + " has failure return code");
		header.setRequestingApplicationId(Constants.RETURN_APPLICATION_ID_DASHBOARD);
		header.setReturnCode("F");
		dto.setResponseHeader(header);
		
		return dto;
	}
	
	private String objToXMLToString(DashboardResponseDto dashboardResponseDto) {
		OutputStream os = new ByteArrayOutputStream();
		try {
			jc = JAXBContext.newInstance(DashboardResponseDto.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(dashboardResponseDto, os);
			os.flush();
			os.close();
		} catch (JAXBException | IOException e) {
			logger.error("JAXBException or IOException while using Marsheller in method objToXMLT0String {}",e);
		}
		return os.toString();	
	}
}
