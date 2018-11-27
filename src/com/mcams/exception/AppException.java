package com.mcams.exception;

@SuppressWarnings("serial")
public class AppException extends Exception {
	String message;
	
	/**
	 * @param msg Exception message in string
	 */
	public AppException(String msg) {
		message = msg;
	}
	
	@Override
	public String getMessage(){
		return this.message;
	}

}