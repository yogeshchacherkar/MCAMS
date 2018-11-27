package com.mcams.service;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 *  Interface contains all functions of Validation service
 *
 */
public interface IValidationService {

	/**Method to validate User Id
	 * @param userId User Id in integer
	 * @return boolean value true if userId matches the pattern else returns false
	 */
	boolean validateUserId(String userId);
	
	/**Method to validate choice
	 * @param input choice input in String
	 * @return integer input if input matches to the pattern else returns 0
	 */
	int validateChoice(String input);
	
	/**Method to validate name
	 * @param name name in String
	 * @return  boolean value true if name matches the pattern else returns false
	 */
	boolean validateName(String name);

	/**Method to validate caeIpi number
	 * @param caeIpi caeIpi in String
	 * @return  boolean value true if caeIpi matches the pattern else returns false
	 */
	boolean validateCaeIpi(String caeIpi);

	/**Method to validate date
	 * @param date Date in string
	 * @return localDate if entered date matches the pattern else returns null
	 */
	LocalDate validateDate(String date);
	
	/**Method to validate User name
	 * @param username User name in string
	 * @return boolean value true if User name matches the pattern else returns false
	 */
	boolean validateUsername(String username);
	
	/**Method to validate password
	 * @param password Password in string
	 * @return boolean value true if password matches the pattern else returns false
	 */
	boolean validatePassword(String password);
	
	/**Method to validate music society Id
	 * @param mSocietyId Music society Id in character
	 * @return boolean value true if Music society Id matches the pattern else returns false
	 */
	boolean validateMSocietyId(char[] mSocietyId);
	
	/**Method to validate duration
	 * @param time Time in string
	 * @return Time in string
	 */
	LocalTime validateDuration(String time);
	
	/**Method to validate answer
	 * @param answer Answer in string
	 * @return boolean value true if answer matches the pattern else returns false
	 */
	boolean validateAnswer(String answer);
	
}
