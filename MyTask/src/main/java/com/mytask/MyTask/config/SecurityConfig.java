package com.mytask.MyTask.config;

import com.mytask.MyTask.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;
	private final DataSource dataSource;

	// Inicializa os serviços para autenticação e acesso ao banco de dados.
	public SecurityConfig(CustomUserDetailsService userDetailsService, DataSource dataSource) {
		this.userDetailsService = userDetailsService;
		this.dataSource = dataSource;
	}

	// Define o encoder de senha (criptografar e verificar senhas).
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Gerenciador de autenticação do usuário e encoder de senha.
	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder()).and().build();
	}

	// Filtros de segurança.
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/", "/register", "/css/**", "/js/**", "/images/**", "/webjars/**", "/h2-console/**")
				.permitAll().anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/dashboard")
						.permitAll())
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login?logout").deleteCookies("JSESSIONID").invalidateHttpSession(true)
						.permitAll())
				.rememberMe(remember -> remember.tokenRepository(persistentTokenRepository())
						.tokenValiditySeconds(86400).userDetailsService(userDetailsService))
				.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
				.headers(headers -> headers.frameOptions().sameOrigin());
		return http.build();
	}

	// Configura o repositório de tokens persistentes usado pela funcionalidade "remember-me".
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}

}