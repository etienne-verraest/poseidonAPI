package com.poseidon.app.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.User;
import com.poseidon.app.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	/**
	 * Spring Security implemented method
	 * Allows user connection by using the username provided
	 *
	 * @param username							The username that tries to connect to the application
	 * @throws UsernameNotFoundException		If the username doesn't exist in database
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			return new User(user.get());
		}
		throw new UsernameNotFoundException("User not found : " + username);
	}

}
