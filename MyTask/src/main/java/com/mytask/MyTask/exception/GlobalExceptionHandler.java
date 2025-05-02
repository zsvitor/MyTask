package com.mytask.MyTask.exception;

import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public String handleResourceNotFoundException(ResourceNotFoundException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		return "error/404";
	}

	@ExceptionHandler(BindException.class)
	public String handleValidationExceptions(BindException ex, RedirectAttributes redirectAttributes) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		redirectAttributes.addFlashAttribute("validationErrors", errors);
		redirectAttributes.addFlashAttribute("errorMessage", "Erro de validação. Por favor, verifique os campos.");
		return "redirect:back";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgumentException(IllegalArgumentException ex, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
		return "redirect:back";
	}

	@ExceptionHandler(Exception.class)
	public String handleGenericException(Exception ex, Model model) {
		model.addAttribute("errorMessage", "Ocorreu um erro inesperado: " + ex.getMessage());
		return "error/500";
	}

}