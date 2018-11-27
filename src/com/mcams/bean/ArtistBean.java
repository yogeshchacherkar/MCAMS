
package com.mcams.bean;

import java.time.LocalDate;
/**
 * This class contains artist attributes and methods
 *
 */

public class ArtistBean {
	private int id;
	private String name;
	private LocalDate bornDate;
	private LocalDate diedDate;
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
	 * @return bornDate in Local date
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
	 * @return diedDate in Local date
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
	 * @return id in integer who created the artist
	 */
	public int getCreatedBy() {
		return createdBy;
	}
	
	/**
	 * @param createdBy to set in Local date
	 */
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	/**
	 * @return date in Local date when artist is created
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
	 * @return id in integer who update the artist details
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
	 * @return date in Local date when artist details are updated
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
