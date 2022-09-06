package com.poseidon.app.domain;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Data
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Generated // Tells Coverage that this class is generated with Lombok
public class User implements UserDetails {

	// This constructor is used for connection
	public User(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.role = user.getRole();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Integer id;

	@Column
	private String username;

	@Column
	private String password;

	@Column
	private String fullname;

	@Column
	private String role;

	// Below are Spring Security related methods

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority(role));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
