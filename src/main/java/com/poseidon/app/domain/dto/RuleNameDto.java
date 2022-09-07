package com.poseidon.app.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RuleNameDto {

	private Integer id;

	private String name;

	private String description;

	private String json;

	private String template;

	private String sqlStr;

	private String sqlPart;
}
