package com.poseidon.app.domain.dto;

import com.poseidon.app.validation.IsDecimal;
import com.poseidon.app.validation.IsInteger;

import lombok.Data;

@Data
public class CurvePointDto {

	private Integer id;

	@IsInteger
	private String curveId;

	@IsDecimal
	private String term;

	@IsDecimal
	private String value;
}
