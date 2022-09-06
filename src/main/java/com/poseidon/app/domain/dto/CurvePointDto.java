package com.poseidon.app.domain.dto;

import com.poseidon.app.validation.Numeric;
import com.poseidon.app.validation.IsInteger;

import lombok.Data;

@Data
public class CurvePointDto {

	private Integer id;

	@IsInteger
	private String curveId;

	@Numeric
	private String term;

	@Numeric
	private String value;
}
