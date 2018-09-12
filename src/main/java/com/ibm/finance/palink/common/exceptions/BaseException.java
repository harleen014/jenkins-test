package com.ibm.finance.palink.common.exceptions;

public class BaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6827463133345482535L;
	protected ErrorMessage errorMessage;
	protected int errorCode;

	public BaseException(ErrorMessage msg) {
		this(msg, (Throwable) null);
	}

	public BaseException(ErrorMessage msg, Throwable cause) {
		super(msg.toString(), cause);
		errorMessage = msg;
	}

	public BaseException(int errCode, ErrorMessage msg, Throwable cause) {
		super(msg.toString(), cause);
		errorMessage = msg;
		errorCode = errCode;
	}

	
	public BaseException(int errorCode, ErrorMessage msg) {
		super(msg.toString());
		this.errorCode = errorCode;
		this.errorMessage = msg;
				
	}
	public ErrorMessage getErrorMessage() {
		return errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
