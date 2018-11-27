package com.mcams.bean;
/**
 * This class contains attributes and methods of SongBean
 *
 */
import java.time.LocalDate;
import java.time.LocalTime;

public class SongBean {
	private int id;
	private String name;
	private LocalTime duration;
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
	 * @return duration in Local time
	 */
	public LocalTime getDuration() {
		return duration;
	}
	/**
	 * @param duration to set in Local time
	 */
	public void setDuration(LocalTime duration) {
		this.duration = duration;
	}
	/**
	 * @return id in integer who created song
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
	 * @return date in Local date when song is created
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
	 * @return id in integer who update the song
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
	 * @return date in Local date when song details updated
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
