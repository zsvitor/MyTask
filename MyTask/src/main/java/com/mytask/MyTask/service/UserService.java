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

	// Método para criar um novo usuário.
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

	// Método para buscar um usuário pelo ID.
	public User getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o ID: " + id));
	}

	// Método para buscar um usuário pelo Gmail.
	public Optional<User> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	// Método para buscar todos os usuários.
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// Método para atualizar os dados de um usuário pelo ID.
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

	// Método para remover um usuário pelo ID.
	public void deleteUser(Long id) {
		User user = getUserById(id);
		userRepository.delete(user);
	}

}