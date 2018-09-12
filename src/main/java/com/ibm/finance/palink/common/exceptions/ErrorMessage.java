package com.ibm.finance.palink.common.exceptions;

public class ErrorMessage {

	private String key;
	private Object[] parameters;

	public ErrorMessage(String key) {
		this(key, new Object[0]);
	}

	public ErrorMessage(String key, Object messageArg1) {
		this(key, new Object[] { messageArg1 });
	}

	public ErrorMessage(String aName, Object messageArg1, Object messageArg2) {
		this(aName, new Object[] { messageArg1, messageArg2 });
	}

	public ErrorMessage(String key, Object[] theParameters) {
		this.key = key;
		setParameters(theParameters);
	}

	private void setParameters(Object[] theParameters) {
		parameters = theParameters == null ? new Object[0] : theParameters;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		for (int i = 0; i < parameters.length; ++i) {
			sb.append(":");
			sb.append(parameters[i]);
		}
		return sb.toString();
	}

	public String getKey() {
		return key;
	}

	public Object[] getParameters() {
		return parameters;
	}

}
