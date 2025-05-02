package com.mytask.MyTask.service;

import com.mytask.MyTask.dto.UserDTO;
import com.mytask.MyTask.exception.ResourceNotFoundException;
import com.mytask.MyTask.model.User;
import com.mytask.MyTask.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User createUser(UserDTO userDTO) {
		if (userRepository.existsByEmail(userDTO.getEmail())) {
			throw new IllegalArgumentException("E-mail já cadastrado");
		}
		User user = new User();
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
		user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		return userRepository.save(user);
	}

	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
	}

	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User updateUser(Long id, UserDTO userDTO) {
		User user = getUserById(id);
		user.setName(userDTO.getName());
		if (!user.getEmail().equals(userDTO.getEmail()) && userRepository.existsByEmail(userDTO.getEmail())) {
			throw new IllegalArgumentException("E-mail já cadastrado");
		}
		user.setEmail(userDTO.getEmail());
		if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
			user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		}
		return userRepository.save(user);
	}

	public void deleteUser(Long id) {
		User user = getUserById(id);
		userRepository.delete(user);
	}

}