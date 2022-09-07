package com.poseidon.app.domain.dto;

import com.poseidon.app.validation.Numeric;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TradeDto {

	private Integer id;

	private String account;

	private String type;

	@Numeric
	private Double buyQuantity;
}
