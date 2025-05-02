package com.mytask.MyTask.controller;

import com.mytask.MyTask.dto.LoginDTO;
import com.mytask.MyTask.dto.UserDTO;
import com.mytask.MyTask.model.User;
import com.mytask.MyTask.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MainController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String home() {
		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
			return "redirect:/dashboard";
		}
		return "home";
	}

	@GetMapping("/login")
	public String loginPage(Model model) {
		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
			return "redirect:/dashboard";
		}
		model.addAttribute("loginDTO", new LoginDTO());
		return "login";
	}

	@GetMapping("/register")
	public String registerPage(Model model) {
		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& !SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
			return "redirect:/dashboard";
		}
		model.addAttribute("userDTO", new UserDTO());
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "register";
		}
		try {
			userService.createUser(userDTO);
			redirectAttributes.addFlashAttribute("successMessage",
					"Cadastro realizado com sucesso! Faça login para continuar.");
			return "redirect:/login";
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "register";
		}
	}

	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	@GetMapping("/profile")
	public String profile(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.getUserByEmail(auth.getName()).orElseThrow();
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setName(user.getName());
		userDTO.setEmail(user.getEmail());
		model.addAttribute("userDTO", userDTO);
		return "profile";
	}

	@PostMapping("/profile")
	public String updateProfile(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "profile";
		}
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = userService.getUserByEmail(auth.getName()).orElseThrow();
			userService.updateUser(user.getId(), userDTO);
			redirectAttributes.addFlashAttribute("successMessage", "Perfil atualizado com sucesso!");
			return "redirect:/profile";
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "profile";
		}
	}

}