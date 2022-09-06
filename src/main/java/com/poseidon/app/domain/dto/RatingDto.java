package com.poseidon.app.domain.dto;

import lombok.Data;

@Data
public class RatingDto {

	private Integer id;

	private String moodysRating;

	private String sandPRating;

	private String fitchRating;

	private Integer orderNumber;
}
