package com.mcams.bean;

import java.time.LocalDate;

/**
 * This class contains attributes and methods of user bean
 *
 */
public class UserBean {
	private int userId;
	private String userName;
	private String userPassword;
	private int secQueId;
	private String secQueAnswer;
	private int createdBy;
	private LocalDate createdOn;
	private int updatedBy;
	private LocalDate updatedOn;
	
	/**
	 * @return user Id in integer
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
	 * @return user name in string
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName to set in string
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return user password in string
	 */
	public String getUserPassword() {
		return userPassword;
	}
	/**
	 * @param userPassword to set in string
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
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
	 * @return security question answer in string
	 */
	public String getSecQueAnswer() {
		return secQueAnswer;
	}
	/**
	 * @param secQueAnswer to set in string
	 */
	public void setSecQueAnswer(String secQueAnswer) {
		this.secQueAnswer = secQueAnswer;
	}
	/**
	 * @return Id in integer
	 */
	public int getCreatedBy() {
		return createdBy;
	}
	/**
	 * @param createdBy to set in integer
	 */
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return created date in Local date
	 */
	
	public LocalDate getCreatedOn() {
		return createdOn;
	}
	/**
	 * @param createdOn to set in Local date
	 */
	public void setCreatedOn(LocalDate createdOn) {
		this.createdOn = createdOn;
	}
	/**
	 * @return user Id in integer 
	 */
	public int getUpdatedBy() {
		return updatedBy;
	}
	/**
	 * @param updatedBy to set in integer
	 */
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	/**
	 * @return updated date in Local date
	 */
	public LocalDate getUpdatedOn() {
		return updatedOn;
	}
	/**
	 * @param updatedOn to set in Local date
	 */
	public void setUpdatedOn(LocalDate updatedOn) {
		this.updatedOn = updatedOn;
	}
	
}
