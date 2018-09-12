package com.ibm.finance.palink.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ibm.finance.palink.common.exceptions.BaseException;
import com.ibm.finance.palink.common.exceptions.ErrorMessage;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DashboardException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public DashboardException(String msg) {
		this(new ErrorMessage(msg), (Throwable) null);
	}

	public DashboardException(ErrorMessage msg) {
		this(msg, (Throwable) null);
	}

	public DashboardException(ErrorMessage msg, Throwable throwable) {
		super(msg, throwable);
		errorMessage = msg;
	}
}
