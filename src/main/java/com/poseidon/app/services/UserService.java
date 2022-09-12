package com.poseidon.app.services;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poseidon.app.domain.User;
import com.poseidon.app.domain.dto.UserDto;
import com.poseidon.app.exceptions.UserServiceException;
import com.poseidon.app.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	ModelMapper modelMapper;

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

	/**
	 * Get a list of every users registered on the application
	 *
	 * @return									List<User> : Existing users
	 */
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	/**
	 * Find a user by its ID
	 *
	 * @param userId							The user's ID we want to get
	 * @return									The user if it exists in database
	 * @throws UserServiceException				Thrown if user doesn't exist
	 */
	public User findUserById(Integer userId) throws UserServiceException {
		Optional<User> user = userRepository.findById(userId);
		if (userId != null && user.isPresent()) {
			return user.get();
		}
		throw new UserServiceException("Could not find user with id : " + userId);
	}

	/**
	 * Create a user and persist it into the database
	 *
	 * @param userEntity						The user entity we want to create
	 * @return									True if the creation was successful
	 * @throws UserServiceException				Thrown if username is already taken
	 */
	public boolean createUser(User userEntity) throws UserServiceException {
		if (userEntity != null && !userRepository.findByUsername(userEntity.getUsername()).isPresent()) {
			userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
			userRepository.save(userEntity);

			log.info("[USER SERVICE] New user with username : '{}' and role '{}' has been created",
					userEntity.getUsername(), userEntity.getRole());
			return true;
		}
		throw new UserServiceException("Username is already taken");
	}

	/**
	 * Updates an existing user and persist it into the database
	 *
	 * @param userId							The user's ID we want to get
	 * @param userEntityUpdated					The new information about the user
	 * @return									True if the update was successful
	 * @throws UserServiceException				Thrown if ID is not found
	 */
	public boolean updateUser(Integer userId, User userEntityUpdated) throws UserServiceException {
		Optional<User> user = userRepository.findById(userId);
		if (userId != null && user.isPresent()) {

			userEntityUpdated.setId(userId);
			userEntityUpdated.setPassword(passwordEncoder.encode(userEntityUpdated.getPassword()));
			userRepository.save(userEntityUpdated);

			log.info("[USER SERVICE] Updated user with username : '{}'", userEntityUpdated.getUsername());
			return true;
		}
		throw new UserServiceException("Could not find user with id : " + userId);
	}

	/**
	 * Deletes an user by its ID
	 *
	 * @param userId							The user's ID we want to delete
	 * @return									True if the deletion was successful
	 * @throws UserServiceException				Thrown if ID is not found
	 */
	public boolean deleteUser(Integer userId) throws UserServiceException {
		Optional<User> user = userRepository.findById(userId);
		if (userId != null && user.isPresent()) {
			userRepository.delete(user.get());
			log.info("[USER SERVICE] Deleted user with username : '{}'", user.get().getUsername());
			return true;
		}
		throw new UserServiceException("Could not find user with id : " + userId);
	}

	public User convertDtoToEntity(UserDto userDto) {
		return modelMapper.map(userDto, User.class);
	}

	public UserDto convertEntityToDto(User userEntity) {
		return modelMapper.map(userEntity, UserDto.class);
	}
}
