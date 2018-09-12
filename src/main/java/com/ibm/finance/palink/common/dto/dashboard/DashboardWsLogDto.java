package com.ibm.finance.palink.common.dto.dashboard;

import java.util.Date;

import com.ibm.finance.palink.common.dto.BaseDto;

public class DashboardWsLogDto implements BaseDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7519475872886464865L;

	private Long id;
	
	private String quoteId;
	
	private String responseString;
	
	private Date receivedDate;
	
	private Date respondedDate;
	
	private String returnCode;
	
	private Short errorCode;
	
	private String errorDescription;
	
	private Date created;
	
	private String inputQuoteType;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public String getResponseString() {
		return responseString;
	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}


	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Date getRespondedDate() {
		return respondedDate;
	}

	public void setRespondedDate(Date respondedDate) {
		this.respondedDate = respondedDate;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public Short getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Short errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	public String getInputQuoteType() {
		return inputQuoteType;
	}

	public void setInputQuoteType(String inputQuoteType) {
		this.inputQuoteType = inputQuoteType;
	}

	@Override
	public String toString() {
		return "DashboardWsLog [id=" + id + ", quoteId=" + quoteId
				+ ", responseString=" + responseString + ", received="
				+ receivedDate + ", respondedDate=" + respondedDate + ", returnCode="+returnCode+ ", errorCode=" +errorCode+ ", errorDescription= " +errorDescription+
				", created=" +created +"]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((quoteId == null) ? 0 : quoteId.hashCode());
		result = prime * result
				+ ((responseString == null) ? 0 : responseString.hashCode());
		result = prime * result
				+ ((receivedDate == null) ? 0 : receivedDate.hashCode());
		result = prime * result
				+ ((respondedDate == null) ? 0 : respondedDate.hashCode());
		result = prime * result
				+ ((returnCode == null) ? 0 : returnCode.hashCode());
		result = prime * result
				+ ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result
				+ ((errorDescription == null) ? 0 : errorDescription.hashCode());
		result = prime * result
				+ ((created == null) ? 0 : created.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DashboardWsLogDto other = (DashboardWsLogDto) obj;
		if (quoteId == null) {
			if (other.quoteId != null)
				return false;
		} else if (!quoteId.equals(other.quoteId)) {
			return false;
		}
		if (responseString == null) {
			if (other.responseString!= null)
				return false;
		} else if (!responseString.equals(other.responseString)) {
			return false;
		}
		if (receivedDate == null) {
			if (other.receivedDate != null)
				return false;
		} else if (!receivedDate.equals(other.receivedDate)) {
			return false;
		}
		if (respondedDate == null) {
			if (other.respondedDate != null)
				return false;
		} else if (!respondedDate.equals(other.respondedDate)) {
			return false;
		}
		if (returnCode == null) {
			if (other.returnCode != null)
				return false;
		} else if (!returnCode.equals(other.returnCode)) {
			return false;
		}
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode)) {
			return false;
		}
		if (errorDescription == null) {
			if (other.errorDescription != null) {
				return false;
			}
		} else if (!errorDescription.equals(other.errorDescription)) {
			return false;
		}
		if (created == null) {
			if (other.created != null) {
				return false;
			}
		} else if (!created.equals(other.created)) {
			return false;
		}

		return true;
	}
	
}
