package com.poseidon.app.domain.dto;

import javax.validation.constraints.NotBlank;

import com.poseidon.app.validation.Numeric;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BidDto {

	private Integer id;

	@NotBlank(message = "Account is mandatory")
	private String account;

	@NotBlank(message = "Type is mandatory")
	private String type;

	@Numeric
	private String bidQuantity;
}
