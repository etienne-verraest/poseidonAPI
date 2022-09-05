package com.poseidon.app.domain.dto;

import javax.validation.constraints.NotBlank;

import com.poseidon.app.validation.Password;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private Integer id;

	@NotBlank(message = "Username is mandatory")
	private String username;

	@Password
	private String password;

	@NotBlank(message = "Full name is mandatory")
	private String fullname;

	@NotBlank(message = "Role is mandatory")
	private String role;
}
