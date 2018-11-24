package com.mcams.service;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IValidationService {

	boolean validateUserId(String userId);
	int validateChoice(String input);
	boolean validateName(String name);
	boolean validateCaeIpi(String caeIpi);
	LocalDate validateDate(String nextLine);
	boolean validateUsername(String username);
	boolean validatePassword(String password);
	boolean validateMSocietyId(char[] mSocietyId);
	LocalTime validateDuration(String time);
	boolean validateAnswer(String answer);
	
}
