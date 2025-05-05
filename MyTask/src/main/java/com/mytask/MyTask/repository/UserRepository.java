package com.mytask.MyTask.repository;

import com.mytask.MyTask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	// Busca um usuário pelo seu endereço de email (retorna um Optional - pode conter o usuário ou estar vazio).
	Optional<User> findByEmail(String email);

	// Verifica se já existe um usuário cadastrado com o endereço de email especificado.
	boolean existsByEmail(String email);
	
}