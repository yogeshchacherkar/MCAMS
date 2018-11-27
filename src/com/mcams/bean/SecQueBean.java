package com.mcams.bean;

/**
 * This class contains security question bean attributes and methods
 *
 */
public class SecQueBean {
	private int secQueId;
	private String secQuestion;
	
	/**
	 * @return security question Id in integer
	 */
	public int getSecQueId() {
		return secQueId;
	}
	/**
	 * @param secQueId to set in integer
	 */
	public void setSecQueId(int secQueId) {
		this.secQueId = secQueId;
	}
	/**
	 * @return security question in string
	 */
	public String getSecQuestion() {
		return secQuestion;
	}
	/**
	 * @param secQuestion to set in string
	 */
	public void setSecQuestion(String secQuestion) {
		this.secQuestion = secQuestion;
	}
	
}
