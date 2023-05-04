package com.dojo.challenge.imageprocessingservice.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.dojo.challenge.imageprocessingservice.exceptions.custom.BadRequestException;
import com.dojo.challenge.imageprocessingservice.exceptions.custom.BusinessException;

public class DateValidator {
	
	public static void validateRangeDate(LocalDate startDate, LocalDate endDate) throws BusinessException {
		if(startDate.isAfter(endDate)) {
    		throw new BusinessException("The start date is greater than the end date");
    	}
	}
	
	public static LocalDate validateDateFormat(String date) {
		LocalDate dateFormat;
		try {
			dateFormat =LocalDate.parse(date);
		}catch(DateTimeParseException e){
			throw new BadRequestException("Invalid date, the format must be be yyyy-mm-dd");
		}
		return dateFormat;
	}

}
