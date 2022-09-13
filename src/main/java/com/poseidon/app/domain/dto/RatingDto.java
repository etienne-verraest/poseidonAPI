package com.poseidon.app.domain.dto;

import javax.validation.constraints.NotBlank;

import com.poseidon.app.validation.IsInteger;

import lombok.Data;

@Data
public class RatingDto {

	private Integer id;

	@NotBlank(message = "Moodys Rating is mandatory")
	private String moodysRating;

	@NotBlank(message = "SandP Rating is mandatory")
	private String sandPRating;

	@NotBlank(message = "Fitch Rating is mandatory")
	private String fitchRating;

	@IsInteger
	private Integer orderNumber;
}
