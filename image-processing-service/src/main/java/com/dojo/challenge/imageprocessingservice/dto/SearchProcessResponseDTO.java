package com.dojo.challenge.imageprocessingservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SearchProcessResponseDTO {
	
	String status;
	LocalDateTime startDateTime;
	LocalDateTime EndDateTime;

}
