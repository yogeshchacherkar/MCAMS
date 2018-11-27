package com.mcams.service;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *  Interface contains all functions of Validation service
 *
 */
public interface IValidationService {

	/**
	 * @param userId User Id in integer
	 * @return boolean value true if userId matches the pattern else returns false
	 */
	boolean validateUserId(String userId);
	
	/**
	 * @param input choice input in String
	 * @return integer input if input matches to the pattern else returns 0
	 */
	int validateChoice(String input);
	
	/**
	 * @param name name in String
	 * @return  boolean value true if name matches the pattern else returns false
	 */
	boolean validateName(String name);

	/**
	 * @param caeIpi caeIpi in String
	 * @return  boolean value true if caeIpi matches the pattern else returns false
	 */
	boolean validateCaeIpi(String caeIpi);

	/**
	 * @param date Date in string
	 * @return localDate if entered date matches the pattern else returns null
	 */
	LocalDate validateDate(String date);
	
	/**
	 * @param username User name in string
	 * @return boolean value true if User name matches the pattern else returns false
	 */
	boolean validateUsername(String username);
	
	/**
	 * @param password Password in string
	 * @return boolean value true if password matches the pattern else returns false
	 */
	boolean validatePassword(String password);
	
	/**
	 * @param mSocietyId Music society Id in character
	 * @return boolean value true if Music society Id matches the pattern else returns false
	 */
	boolean validateMSocietyId(char[] mSocietyId);
	
	/**
	 * @param time Time in string
	 * @return Time in string
	 */
	LocalTime validateDuration(String time);
	
	/**
	 * @param answer Answer in string
	 * @return boolean value true if answer matches the pattern else returns false
	 */
	boolean validateAnswer(String answer);
	
}
