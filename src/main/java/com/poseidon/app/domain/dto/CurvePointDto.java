package com.poseidon.app.domain.dto;

import com.poseidon.app.validation.Numeric;
import com.poseidon.app.validation.WholeNumber;

import lombok.Data;

@Data
public class CurvePointDto {

	private Integer id;

	@WholeNumber
	private String curveId;

	@Numeric
	private String term;

	@Numeric
	private String value;
}
