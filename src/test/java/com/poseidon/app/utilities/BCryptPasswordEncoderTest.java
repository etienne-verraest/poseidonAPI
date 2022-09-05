package com.poseidon.app.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordEncoderTest {

	@Test
	public void testPasswordEncryption() {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String pw = passwordEncoder.encode("123456");
		boolean match = passwordEncoder.matches("123456", pw);
		assertThat(match).isTrue();
	}

}
