package com.poseidon.app.domain.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RuleDto {

	private Integer id;

	@NotBlank(message = "Name is mandatory")
	private String name;

	@NotBlank(message = "Description is mandatory")
	private String description;

	@NotBlank(message = "JSON is mandatory")
	private String json;

	@NotBlank(message = "Template is mandatory")
	private String template;

	@NotBlank(message = "SQL String is mandatory")
	private String sqlStr;

	@NotBlank(message = "SQL Part is mandatory")
	private String sqlPart;
}
