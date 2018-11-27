package com.mcams.bean;

/**
 * This class contains authentication attributes and methods
 *
 */
public class AuthenticationBean {
	private int userId;
	private String password;

	/**
	 * @return user id in integer
	 */
	
	public int getUserId() {
		return userId;
	}
	/**
	 * @param userId to set in integer
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}
	/**
	 * @return password in string
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password to set in string
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
}
