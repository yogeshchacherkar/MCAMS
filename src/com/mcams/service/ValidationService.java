package com.mcams.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class ValidationService implements IValidationService {
	
	@Override
	public boolean validateUserId(String userId) {
		if(userId.matches("[0-9]{6}"))	return true;
		else return false;
	}
	
	@Override
	public int validateChoice(String input) {
		if(input.matches("[0-9]{1,2}"))
			return Integer.parseInt(input);
		else return 0;
	}
	
	@Override
	public boolean validateName(String name) {
		if(name.matches("[a-zA-Z\\s\\.]{3,50}")) return true;
		else return false;
	}
	
	@Override
	public LocalDate validateDate(String date) {
		if(date.matches("^\\d{2}/\\d{2}/\\d{4}")){
			LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			
			if(localDate.isAfter(LocalDate.now())) return null;
			
			return localDate;
		}
		else
			return null;
	}
	
	@Override
	public boolean validateCaeIpi(String caeIpi) {
		if(caeIpi.matches("[a-zA-Z0-9\\s]{3,10}")) return true;
		else return false;
	}

	@Override
	public boolean validateMSocietyId(char[] mSocietyId) {
		if(mSocietyId.length==3) {
			return true;
		}
		else return false;
	}

	@Override
	public LocalTime validateDuration(String time) {
		if(time.matches("[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}")) {
			time = "12:"+time;
			return LocalTime.parse(time,DateTimeFormatter.ofPattern("H:m:s"));
		}
		else return null;		
	}
	
	@Override
	public boolean validateUsername(String username) {
		if(username.matches("^[a-zA-Z][[a-zA-Z0-9]\\.]{3,50}")) return true;
		else return false;
	}
	
	@Override
	public boolean validatePassword(String password) {
		if(password.length()>8 && password.length()<50) return true;
		else return false;
	}

	@Override
	public boolean validateAnswer(String answer) {
		if(answer.matches("[a-zA-Z0-9]{1,50}")) return true;
		else return false;
	}
	
}
