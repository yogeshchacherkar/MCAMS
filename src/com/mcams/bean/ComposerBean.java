package com.mcams.bean;

import java.time.LocalDate;
/**
 * This class contains composer attributes and methods
 *
 */
public class ComposerBean {
	private int id;
	private String name;
	private LocalDate bornDate;
	private LocalDate diedDate;
	private String caeipiNumber;
	private char[] musicSocietyId;
	private int createdBy;
	private LocalDate createdOn;
	private int updatedBy;
	private LocalDate updatedOn;
	private int deletedFlag;
	
	
	/**
	 * @return id in integer
	 */
	
	public int getId() {
		return id;
	}
	/**
	 * @param id to set in integer
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return name in string
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name to set in string
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Born date in Local date
	 */
	public LocalDate getBornDate() {
		return bornDate;
	}
	/**
	 * @param bornDate to set in Local date
	 */
	public void setBornDate(LocalDate bornDate) {
		this.bornDate = bornDate;
	}
	/**
	 * @return Died date in Local date
	 */
	public LocalDate getDiedDate() {
		return diedDate;
	}
	/**
	 * @param diedDate to set in Local date
	 */
	public void setDiedDate(LocalDate diedDate) {
		this.diedDate = diedDate;
	}
	/**
	 * @return caeipiNumber in String
	 *
	 */
	public String getCaeipiNumber() {
		return caeipiNumber;
	}
	/**
	 * @param caeipiNumber to set in string
	 */
	public void setCaeipiNumber(String caeipiNumber) {
		this.caeipiNumber = caeipiNumber;
	}
	/**
	 * @return music society id in character
	 */
	public char[] getMusicSocietyId() {
		return musicSocietyId;
	}
	/**
	 * @param musicSocietyId to set in character
	 */
	public void setMusicSocietyId(char[] musicSocietyId) {
		this.musicSocietyId = musicSocietyId;
	}
	/**
	 * @return id in integer who created composer
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
	 * @return date in Local date when composer is created
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
	 * @return id in integer who updated composer
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
	 * @return date in Local date when composer is updated
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
	/**
	 * @return deleted flag in integer
	 */
	public int getDeletedFlag() {
		return deletedFlag;
	}
	/**
	 * @param deletedFlag to set in integer
	 */
	public void setDeletedFlag(int deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
	
}
